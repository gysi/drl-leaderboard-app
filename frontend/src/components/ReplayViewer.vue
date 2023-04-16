<template>
  <div ref="container" class="three-container">
    <div style="position: absolute">
      <slot name="header"></slot>
      <div class="q-mt-md q-ml-md"><button>Test</button></div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref, watch } from 'vue';
import * as THREE from 'three';
import { OrbitControls } from 'three/addons/controls/OrbitControls.js';
import { Text } from 'troika-three-text';
import axios from 'axios';
import pako from 'pako';
import { formatMilliSeconds } from 'src/modules/LeaderboardFunctions.js';
import {lessThanEqual} from "three/nodes";

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

let loadingTrackPromise = null;
watch(() => props.trackId, (val) => {
  console.log("replayviewer trackId changed", val);
  if(val){
    disposeSceneObjects();
    currentSplineColor = 0;
    loadingTrackPromise = loadTrack(val);
  }
});

watch(() => props.replayUrl, (val) => {
  console.log("replayviewer replayUrl changed", val);
  if(val){
    loadReplay(props.trackId, val, splineColors[currentSplineColor]);
    if(currentSplineColor >= splineColors.length - 1){
      currentSplineColor = 0;
    } else {
      currentSplineColor++;
    }
  }
});

const container = ref(null);

let splineColors = [
  0x00FF00,
  0x71bbff,
  0xff76eb,
  0xffe54c,
  0xcffffd,
];
let currentSplineColor = 0;
let renderer, scene, camera, controls;
let textLabels = [];
let textLabels2 = [];
let disposables = [];
let sceneObjects = [];

// Function to create text labels
function createLabel(scene, text, position, textLabels) {
  const renderedText = new Text();

  renderedText.text = text;
  renderedText.color = 0xFFFFFF;
  renderedText.fontSize = 1;
  renderedText.position.set(position.x, position.y, position.z);
  textLabels.push(renderedText);

  sceneObjects.push(renderedText);
  scene.add(renderedText);
}

const defaultCubeGeometry = new THREE.BoxGeometry(0.2, 0.2, 0.2);
const defaultCubeMaterial = new THREE.MeshBasicMaterial({
  color: 0xff0000,
  // alpha
  transparent: true,
  // opacity
  opacity: 0.5,
  side: THREE.BackSide,
  fog: false,
});
function addCubes(color, scene, events, positionExtractor, scalingExtractor = null, rotationExtractor = null){
  events.forEach((event) => {
    let cube;
    if(scalingExtractor != null){
      const [sx, sy, sz] = scalingExtractor(event);
      const customGeometry = new THREE.BoxGeometry(sx, sy, sz)
      disposables.push(customGeometry);
      cube = new THREE.Mesh(customGeometry, defaultCubeMaterial);
    } else {
      cube = new THREE.Mesh(defaultCubeGeometry, defaultCubeMaterial);
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
    cube.matrixAutoUpdate = false;
    cube.updateMatrix();

    sceneObjects.push(cube);
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
    if (eventSample === 0 || event["event-type"] === 7) {
      return [];
    }
    const vectors = [];
    for (let i = lastEventSample; i < eventSample; i++) {
      vectors.push(new THREE.Vector3(
        dronePosZ[i],
        dronePosY[i],
        dronePosX[i]
      ));
    }
    // const [z, y, x] = eventPositionExtractor(event);
    // vectors.push(new THREE.Vector3(x, y, z));
    lastEventSample = eventSample;
    return vectors;
  });

  // Create a Catmull-Rom spline from the points
  const spline = new THREE.CatmullRomCurve3(points);

  // Generate a geometry from the spline
  // const geometry = new THREE.BufferGeometry().setFromPoints(spline.getPoints(points.length));
  // const geometry = new THREE.BufferGeometry().setFromPoints(points);
  const geometry = new THREE.TubeGeometry( spline, points.length, 0.3, 3, false );
  disposables.push(geometry);

  const material = new THREE.MeshStandardMaterial({ color: color, wireframe: false });
  // const material = new THREE.LineBasicMaterial({ color: color, linewidth: 10 });

  disposables.push(material)
  // Create a line using the geometry and material
  // const line = new THREE.Line(geometry, material);
  const line = new THREE.Mesh(geometry, material);

  line.matrixAutoUpdate = false;
  line.updateMatrix();

  sceneObjects.push(line);
  scene.add(line);
}

function addTrackLabels(scene, checkpoints){
  // Add labels to the checkpoints
  checkpoints.forEach((checkpoint, index) => {
    const position = new THREE.Vector3(checkpoint.position.z, checkpoint.position.y, checkpoint.position.x);
    if (index === 0) {
      createLabel(scene, "Start", position, textLabels);
    } else if (index === checkpoints.length - 1) {
      createLabel(scene, "End", position, textLabels);
    } else {
      createLabel(scene, '#' + (index-1), position, textLabels);
    }
  });
}

function addReplayLabels(scene, events, positionExtractor){
  // Add labels to the events
  let labelArrayOffset = 0;
  let offsetAmount = null;
  let lap = 0;
  events.forEach((event, index) => {
    const isLapStart = event["event-type"] === 7;
    if (isLapStart) lap++;

    // console.log('indexoffset', index, index - (labelArrayOffset + lap))
    let textLabel = textLabels[index - (labelArrayOffset + lap)];
    if (textLabel) {
      const time = formatMilliSeconds(event["event-time"] * 1000);
      textLabel.text += isLapStart ? ` Lap ${lap}` : `\n${time}`;
    } else {
      // console.log('no text label', index);
    }

    if (isLapStart) {
      if (offsetAmount === null) {
        offsetAmount = index - 1;
      }
      labelArrayOffset += offsetAmount;
    }

    // if(index <= 38) {
    //
    //   const [z, y, x] = positionExtractor(event);
    //   const position = new THREE.Vector3(x, y, z);
    //   if (index === 0) {
    //     createLabel(scene, "Start", position, textLabels2);
    //     // } else if (index === events.length - 1) {
    //     //   createLabel(scene, textLabel, position);
    //   } else if (event["event-type"] === 7) {
    //     console.log('lap end2', index)
    //     let time = formatMilliSeconds(event["event-time"] * 1000);
    //     let textLabel = '\n\n\n\n_' + time;
    //     if (isLapStart) {
    //       textLabel = textLabel + ' ' + 'Lap end';
    //     }
    //     createLabel(scene, textLabel, position, textLabels2);
    //   } else {
    //     let time = formatMilliSeconds(event["event-time"] * 1000);
    //     let textLabel = '\n\n\n\n_' + time;
    //     createLabel(scene, textLabel, position, textLabels2);
    //   }
    // }
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
    // if(child['is-lap-start'] === true || child['is-lap-end'] === true) {

    // }
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
      // console.log('gate-index', child['gate-index']);
      // console.log('child', child);
      let checkpoint = {
        position: {
          x: child['local-position'][0],
          y: child['local-position'][1],
          z: child['local-position'][2]
        },
        scaling: {
          x: child['local-scale'][0]*3.1, // scale up a bit to match the size of the checkpoints as close as possible
          y: child['local-scale'][1]*3.1,
          z: child['local-scale'][2]*1
        },
        rotation: [
          child['local-rotation'][0],
          child['local-rotation'][1],
          child['local-rotation'][2],
          child['local-rotation'][3]
        ],
        index: child['gate-index']
      }
      if(child['module-scale']){ // if module scale is set then multiply the checkpoint scale with it
        checkpoint.scaling.x *= child['module-scale'][0]*1.3
        checkpoint.scaling.y *= child['module-scale'][1]*1.3
        checkpoint.scaling.z *= child['module-scale'][2]
      }
      checkPoints.push(checkpoint)
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

let checkPoints = [];
const loadTrack = async (trackId) => {
  const trackDetails = await axios.get(process.env.DLAPP_API_URL+'/tracks/details/'+trackId);
  checkPoints = createCheckpointsFromTrackDetailData(trackDetails.data);

  // Calculate the center of the event positions
  // console.log("checkPoints", checkPoints)
  const center = checkPoints.reduce(
    (acc, event) => {
      const {x, y, z} = event.position;
      acc.x += z;
      acc.y += y;
      acc.z += x;
      return acc;
    },
    { x: 0, y: 0, z: 0 }
  );
  center.x /= checkPoints.length;
  center.y /= checkPoints.length;
  center.z /= checkPoints.length;

  camera.position.set(center.x+25, center.y+25, center.z+25);
  controls.target.set(center.x, center.y, center.z);

  addCubes(0xff0000, scene, checkPoints,
    (event) => [event.position.x, event.position.y, event.position.z],
    (event) => [event.scaling.x, event.scaling.y, event.scaling.z],
    (event) => event.rotation
  )

  addTrackLabels(scene, checkPoints);
}

const loadReplay = async (trackId, replayUrl, splineColor) => {
  await loadingTrackPromise;
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
  // console.log("time markers", markers['time']);

  // addCubes(0x00ff00, scene, replayCheckPointData.events, (event) => event["event-position"])
  addSplines(splineColor, scene, replayCheckPointData.events,
    (event) => event["event-position"],
    markers
  )
  addReplayLabels(scene, replayCheckPointData.events, (event) => event["event-position"])
}

function disposeSceneObjects() {
  if(scene == null){
    return;
  }
  disposables.forEach((disposable) => {
    disposable.dispose();
  });
  sceneObjects.forEach((sceneObject) => {
    scene.remove(sceneObject);
  });
  disposables = [];
  textLabels = []
  sceneObjects = [];
}

onMounted(() => {
  renderer = new THREE.WebGLRenderer();
  renderer.setSize(container.value.offsetWidth, container.value.offsetHeight);
  container.value.appendChild(renderer.domElement);

  scene = new THREE.Scene();
  camera = new THREE.PerspectiveCamera(
    90,
    container.value.offsetWidth / container.value.offsetHeight,
    0.1,
    1000
  );

  controls = new OrbitControls(camera, renderer.domElement);
  controls.panSpeed = 1;
  controls.rotateSpeed = 0.5;
  controls.zoomSpeed = 1.8;
  controls.minDistance = 8;
  controls.maxDistance = 1000;

  const ambientLight = new THREE.AmbientLight(0xFFFFFF, 0.5); // Add ambient light with an intensity of 1
  scene.add(ambientLight);

  // Create a directional light
  const directionalLight = new THREE.DirectionalLight(0xffffff, 0.5);
  directionalLight.position.set(1, 1, 1);
  scene.add(directionalLight);

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
