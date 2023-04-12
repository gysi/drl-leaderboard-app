<template>
  <div ref="container" class="three-container">
    <slot name="header"></slot>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref, watch } from 'vue';
import * as THREE from 'three';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js';
import { Text } from 'troika-three-text';
import axios from 'axios';
import pako from 'pako';

const props = defineProps({
  trackId: {
    type: String,
    default: null
  },
  replayUrl: {
    type: String,
    default: null
  }
});

// // watch trackid and replayUrl
// watch(() => props.trackId, (val) => {
//   console.log("replayviewer trackId changed", val);
//   if(val){
//     loadReplay()
//   }
// });

watch(() => props.replayUrl, (val) => {
  console.log("replayviewer replayUrl changed", val);
  if(val){
    for (let i = disposables.length - 1; i >= 0; i--) {
      disposables[i].dispose();
    }
    loadReplay(props.trackId, val);
  }
});

const container = ref(null);

let renderer, scene, camera, controls;
const textLabels = [];
const disposables = [];

// Function to create text labels
function createLabel(scene, text, position) {
  const renderedText = new Text();

  renderedText.text = text;
  renderedText.color = 0xFFFFFF;
  renderedText.fontSize = 1;
  renderedText.position.set(position.x, position.y, position.z);

  textLabels.push(renderedText);

  scene.add(renderedText);
}

function addCubes(color, scene, events, positionExtractor, scalingExtractor = null, rotationExtractor = null){
  const geometry = new THREE.BoxGeometry(0.2, 0.2, 0.2);
  disposables.push(geometry);
  const material = new THREE.MeshBasicMaterial({
    color: color,
    // alpha
    transparent: true,
    // opacity
    opacity: 0.5,
    side: THREE.BackSide,
    fog: false,
  });
  disposables.push(material);

  events.forEach((event) => {
    let cube;
    if(scalingExtractor != null){
      const [sx, sy, sz] = scalingExtractor(event);
      const customGeometry = new THREE.BoxGeometry(sx, sy, sz)
      disposables.push(customGeometry);
      cube = new THREE.Mesh(customGeometry, material);
    } else {
      cube = new THREE.Mesh(geometry, material);
    }
    const [z, y, x] = positionExtractor(event);
    cube.position.set(x, y, z);
    if(rotationExtractor != null){
      const [r0, r1, r2, r3] = rotationExtractor(event);
      const quaternion = new THREE.Quaternion(r2, -r1, r0, r3);
      const coordinateSystemRotation = new THREE.Quaternion();
      coordinateSystemRotation.setFromAxisAngle(new THREE.Vector3(0, 1, 0), Math.PI / 2);
      quaternion.normalize()
      const combinedQuaternion = new THREE.Quaternion().multiplyQuaternions(
        quaternion,
        coordinateSystemRotation
      );
      combinedQuaternion.normalize();
      cube.setRotationFromQuaternion(combinedQuaternion)
    }
    scene.add(cube);
  });
}

function addSplines(color, scene, events, eventPositionExtractor, replayData){
  // Create an array of Vector3 points from the event positions
  let lastEventSample = 0;
  const dronePosX = replayData['drone-px'];
  const dronePosY = replayData['drone-py'];
  const dronePosZ = replayData['drone-pz'];
  const points = events.flatMap((event) => {
    let eventSample = event["event-sample"];
    if (eventSample === 0) {
      return [];
    }
    const vectors = [];
    for (let i = lastEventSample; i <= eventSample; i++) {
      vectors.push(new THREE.Vector3(
        dronePosZ[i],
        dronePosY[i],
        dronePosX[i]
      ));
    }
    const [z, y, x] = eventPositionExtractor(event);
    // console.log('vectors', vectors);
    // console.log('checkpoint', new THREE.Vector3(x, y, z));
    vectors.push(new THREE.Vector3(x, y, z));
    lastEventSample = eventSample;
    return vectors;
  });

  // Create a Catmull-Rom spline from the points
  const spline = new THREE.CatmullRomCurve3(points);

  // Generate a geometry from the spline
  const geometry = new THREE.BufferGeometry().setFromPoints(spline.getPoints(points.length));
  disposables.push(geometry);
  const material = new THREE.LineBasicMaterial({ color: color });
  disposables.push(material)
  // Create a line using the geometry and material
  const line = new THREE.Line(geometry, material);

  scene.add(line);
}

function addLabels(scene, events, positionExtractor){
  // Add labels to the events
  events.forEach((event, index) => {
    const [z, y, x] = positionExtractor(event);
    const position = new THREE.Vector3(x, y, z);

    if (index === 0) {
      createLabel(scene, "Start", position);
    } else if (index === events.length - 1) {
      createLabel(scene, "End", position);
    } else {
      createLabel(scene, event["event-time"].toFixed(2), position);
    }
  });
}

let requestAnimationFrameId = null;
let cancelInProgress = false;
function cancelAnimationFrame() {
  cancelInProgress = true;
  if (requestAnimationFrameId) {
    window.cancelAnimationFrame(requestAnimationFrameId)
    requestAnimationFrameId = null;
  }
  cancelInProgress = false;
}

function animate(scene, camera) {
  if (cancelInProgress) {
    return;
  }
  requestAnimationFrameId = requestAnimationFrame(() => animate(scene, camera));

  // Update text label rotation
  textLabels.forEach(label => {
    label.lookAt(camera.position);
  });

  controls.update();
  renderer.render(scene, camera);
}

function extractJSON(str) {
  var firstOpen, firstClose, candidate;
  firstOpen = str.indexOf('{', firstOpen + 1);
  do {
    firstClose = str.lastIndexOf('}');
    // console.log('firstOpen: ' + firstOpen, 'firstClose: ' + firstClose);
    if(firstClose <= firstOpen) {
      return null;
    }
    do {
      candidate = str.substring(firstOpen, firstClose + 1);
      // console.log('candidate: ' + candidate);
      try {
        var res = JSON.parse(candidate);
        // console.log('...found');
        return [res, firstOpen, firstClose + 1];
      }
      catch(e) {
        // console.log('...failed');
      }
      firstClose = str.substr(0, firstClose).lastIndexOf('}');
    } while(firstClose > firstOpen);
    firstOpen = str.indexOf('{', firstOpen + 1);
  } while(firstOpen != -1);
}

function createCheckpointsFromTrackDetailData(data){
  const checkPoints = []
  data.root.children.forEach((child) => {
    if(child['podium-index'] === 0) {
      checkPoints.push({
        position: {
          x: child['local-position'][0],
          y: child['local-position'][1],
          z: child['local-position'][2]
        },
        scaling: {
          x: 1,
          y: 1,
          z: 1
        },
        rotation: [0,0,0,0],
        index: -1
      })
    } else if(child['gate-index'] != null && child['is-trigger'] === true) {
      checkPoints.push({
        position: {
          x: child['local-position'][0],
          y: child['local-position'][1],
          z: child['local-position'][2]
        },
        scaling: {
          x: child['local-scale'][0]*3, // scale up a bit to match the size of the checkpoints as close as possible
          y: child['local-scale'][1]*3,
          z: child['local-scale'][2]*3
        },
        rotation: [
          child['local-rotation'][0],
          child['local-rotation'][1],
          child['local-rotation'][2],
          child['local-rotation'][3]
        ],
        index: child['gate-index']
      })
      // console.log('child', child['gate-index'], child);
    }
  });
  // sort checkpoints by index
  checkPoints.sort((a, b) => a.index - b.index);
  // console.log('checkPoints', checkPoints);
  return checkPoints
}

function findMarkersAndExtractData(byteArray) {
  const markerPrefixes = {
    'time,': 'time',
    'drone-px,': 'drone-px',
    'drone-py,': 'drone-py',
    'drone-pz,': 'drone-pz',
  }
  const output = {};
  const markerKeys = Object.keys(markerPrefixes);
  let currentMarkerIndex = 0;
  let byteIndex = 0;
  let markerPrefix = markerKeys[currentMarkerIndex];
  while(byteIndex < byteArray.byteLength && currentMarkerIndex < markerKeys.length){
    const byte = byteArray[byteIndex];
    const char = String.fromCharCode(byte);
    if (char === markerPrefix[0]) {
      const possibleMarker = String.fromCharCode.apply(null, byteArray.slice(byteIndex, byteIndex + 20));
      if (possibleMarker.startsWith(markerPrefix)) {
        const splittedMarker = possibleMarker.split(",");
        const bytePartLength = parseInt(splittedMarker[1], 10);
        const markerPart = possibleMarker.substring(0, possibleMarker.indexOf(',4,1')+5);
        const unzippedBytePart = pako.inflate(
          byteArray.slice(byteIndex + markerPart.length,
            byteIndex + markerPart.length + bytePartLength)
        );
        output[markerPrefixes[markerPrefix]] = byteArrayToFloatArray(unzippedBytePart);
        currentMarkerIndex++
        byteIndex += markerPart.length + bytePartLength;
        markerPrefix = markerKeys[currentMarkerIndex];
        continue;
      }
    }
    byteIndex++;
  }

  return output;
}

function byteArrayToFloatArray(byteArray){

// Create an ArrayBuffer with the same byte length as the Uint8Array
  const buffer = new ArrayBuffer(byteArray.length);

// Create a Uint8Array view on the buffer to copy the byteArray data
  const bufferView = new Uint8Array(buffer);
  bufferView.set(byteArray);

// Create a Float32Array view on the buffer to interpret the data as floating point numbers
  const floatArray = new Float32Array(buffer);
  return floatArray
}

function sumMarkerData(markers){
  const time = markers['time'];
  const dronePosX = markers['drone-px'];
  const dronePosY = markers['drone-py'];
  const dronePosZ = markers['drone-pz'];

  const sumFloatArray = function (arr) {
    for (let i = 1; i < arr.length; i++) {
      arr[i] += arr[i - 1];
    }
  }
  sumFloatArray(time);
  sumFloatArray(dronePosX);
  sumFloatArray(dronePosY);
  sumFloatArray(dronePosZ);
}

const loadReplay = async (trackId, replayUrl) => {
  const trackDetails = await axios.get(process.env.DLAPP_API_URL+'/tracks/details/'+trackId);
  const checkPoints = createCheckpointsFromTrackDetailData(trackDetails.data);
  const response = await axios.get(replayUrl, { responseType: 'arraybuffer' });
  const decodedData = new TextDecoder().decode(response.data);
  const replayCheckPointData = extractJSON(decodedData)[0];
  replayCheckPointData.events.unshift({
    "event-data":
      [
        0
      ],
    "event-position":
      [
        checkPoints[0].position.x,
        checkPoints[0].position.y,
        checkPoints[0].position.z
      ],
    "event-sample": 0,
    "event-time": 0,
    "event-type": 1
  });
  let markers = findMarkersAndExtractData(new Uint8Array(response.data));
  sumMarkerData(markers);

  // Calculate the center of the event positions
  const center = replayCheckPointData.events.reduce(
    (acc, event) => {
      const [z, y, x] = event["event-position"];
      if(acc.x === 0 && acc.y === 0 && acc.z === 0){
        acc.x = x;
        acc.y = y;
        acc.z = z;
      }else {
        acc.x += x;
        acc.y += y;
        acc.z += z;
      }
      return acc;
    },
    { x: 0, y: 0, z: 0 }
  );
  center.x /= replayCheckPointData.events.length;
  center.y /= replayCheckPointData.events.length;
  center.z /= replayCheckPointData.events.length;

  camera.position.set(center.x, center.y, center.z); // 200 is an arbitrary distance from the center
  camera.lookAt(center.x, center.y, center.z);

  addCubes(0x00ff00, scene, replayCheckPointData.events, (event) => event["event-position"])
  addCubes(0xff0000, scene, checkPoints,
    (event) => [event.position.x, event.position.y, event.position.z],
    (event) => [event.scaling.x, event.scaling.y, event.scaling.z],
    (event) => event.rotation
  )
  addSplines(0x00ff00, scene, replayCheckPointData.events,
    (event) => event["event-position"],
    markers
  )
  addLabels(scene, replayCheckPointData.events, (event) => event["event-position"])

  // animate(scene, camera);
}

onMounted(() => {
  renderer = new THREE.WebGLRenderer();
  renderer.setSize(container.value.offsetWidth, container.value.offsetHeight);
  container.value.appendChild(renderer.domElement);

  scene = new THREE.Scene();
  camera = new THREE.PerspectiveCamera(
    75,
    container.value.offsetWidth / container.value.offsetHeight,
    0.1,
    1000
  );

  controls = new OrbitControls(camera, renderer.domElement);
  controls.panSpeed = 0.5;
  controls.rotateSpeed = 0.5;
  controls.zoomSpeed = 1;

  animate(scene, camera);
});

onUnmounted(() => {
  cancelAnimationFrame();
  for (let i = disposables.length - 1; i >= 0; i--) {
    disposables[i].dispose();
  }
  controls.dispose();
  renderer.dispose()
});
</script>

<style lang="sass" scoped>
.three-container
  width: 100%
  height: 100%
</style>
