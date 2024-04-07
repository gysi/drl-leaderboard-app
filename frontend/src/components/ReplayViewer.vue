<template>
  <div ref="container" class="three-container">
    <div style="position: absolute">
      <slot name="header"></slot>
<!--      <div class="q-mt-md q-ml-md"><button>Test</button></div>-->
    </div>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref, watch } from 'vue';
import * as THREE from 'three';
import { OrbitControls } from 'three/addons/controls/OrbitControls.js';
import { OBJLoader } from 'three/addons/loaders/OBJLoader.js';
import { Text } from 'troika-three-text';
import axios from 'axios';
import pako from 'pako';
import { formatMilliSeconds } from 'src/modules/LeaderboardFunctions.js';
import {assetLoader, assetObjLoader, getNameByModuleScale} from 'src/modules/ReplayAssetsLoader.js'

const props = defineProps({
  trackId: {
    type: String,
    default: null
  },
  replayData: {
    type: Object,
    default: null
  }
});

let loadingTrackPromise = null;
watch(() => props.trackId, (val) => {
  // console.log("replayviewer trackId changed", val);
  if(val){
    disposeSceneObjects();
    currentSplineColor = 0;
    loadingTrackPromise = loadTrack(val);
  }
});

watch(() => props.replayData, async (replayData) => {
  // console.log("replayviewer replayData changed", replayData);
  if(replayData){
    await loadReplay(props.trackId, replayData, splineColors[currentSplineColor]);
    if(currentSplineColor >= splineColors.length - 1){
      currentSplineColor = 0;
    } else {
      currentSplineColor++;
    }
  }
});

const container = ref(null);

let splineColors = [
  {
    color1: new THREE.Color("rgb(117,252,117)"),
    color2: new THREE.Color("rgb(0,128,0)")
  },
  {
    color1: new THREE.Color("rgb(117,190,255)"),
    color2: new THREE.Color("rgb(0,90,166)")
  },
  {
    color1: new THREE.Color("rgb(255,137,237)"),
    color2: new THREE.Color("rgb(164,0,139)")
  },
  {
    color1: new THREE.Color("rgb(255,236,109)"),
    color2: new THREE.Color("rgb(192,166,0)")
  },
  {
    color1: new THREE.Color("rgb(250,102,102)"),
    color2: new THREE.Color("rgb(129,0,0)")
  },
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
  // color: 0xff0000,
  color: "rgb(255,47,47)",
  // alpha
  transparent: true,
  depthTest: true,
  depthWrite: true,
  // opacity
  opacity: 0.2,
  side: THREE.DoubleSide,
  fog: false,
});
async function loadAndAddAsset(scene, name, px, py, pz, sx, sy, sz, r0, r1, r2, r3){
  if(assetLoader[name].refs){
    const refs = assetLoader[name].refs
    for (let ref of refs) {
      // console.log("REF", ref);
      loadAndAddAsset(scene, ref, px, py, pz, sx, sy, sz, r0, r1, r2, r3)
    }
  }
  const loadLodFunc = assetLoader[name].lod;
  if(loadLodFunc) {
    const object = await assetObjLoader(await loadLodFunc())
    object.matrixAutoUpdate = false;
    object.position.set(px, py, -pz);
    object.scale.set(sx, sz, sy);
    const quaternion = new THREE.Quaternion(-r0, -r1, r2, r3);
    object.quaternion.multiply(quaternion);
    object.quaternion.multiply(new THREE.Quaternion().setFromAxisAngle(new THREE.Vector3(-1, 0, 0), Math.PI / 2))
    object.quaternion.multiply(new THREE.Quaternion().setFromAxisAngle(new THREE.Vector3(0, 0, 1), Math.PI))
    object.updateMatrix();
    sceneObjects.push(object);
    scene.add(object);
  } else {
    const loadTriggerFunc = assetLoader[name].trigger;
    if (loadTriggerFunc) {
      const object = await assetObjLoader(await loadTriggerFunc())
      object.matrixAutoUpdate = false;
      object.children[0].material = defaultCubeMaterial
      object.position.set(px, py, -pz);
      object.scale.set(sx, sz, sy);
      const quaternion = new THREE.Quaternion(-r0, -r1, r2, r3);
      object.quaternion.multiply(quaternion);
      object.quaternion.multiply(new THREE.Quaternion().setFromAxisAngle(new THREE.Vector3(-1, 0, 0), Math.PI / 2))
      object.quaternion.multiply(new THREE.Quaternion().setFromAxisAngle(new THREE.Vector3(0, 0, 1), Math.PI))
      object.updateMatrix();
      sceneObjects.push(object);
      scene.add(object);
      // const box = new THREE.BoxHelper(object, 0xffff00);
      // scene.add(box);
    }
  }
}
async function addCubes(color, scene, checkpoints, positionExtractor, scalingExtractor = null, rotationExtractor = null){
  checkpoints.forEach(async (checkpoint) => {
    const [x, y, z] = positionExtractor(checkpoint);
    const [sx, sy, sz] = scalingExtractor(checkpoint);
    const [r0, r1, r2, r3] = rotationExtractor(checkpoint);
    if(assetLoader[checkpoint.name]){
      loadAndAddAsset(scene, checkpoint.name, x, y, z, sx, sy, sz, r0, r1, r2, r3)
    } else {
      console.error("NO LOD OR TRIGGER FOUND")
      console.log(checkpoint)
      const customGeometry = new THREE.BoxGeometry()
      disposables.push(customGeometry);
      const object = new THREE.Mesh(customGeometry, defaultCubeMaterial);
      object.matrixAutoUpdate = false;
      object.position.set(x, y, -z);
      object.scale.set(sx, sz, sy);
      const quaternion = new THREE.Quaternion(-r0, -r1, r2, r3);
      object.quaternion.multiply(quaternion);
      object.quaternion.multiply(new THREE.Quaternion().setFromAxisAngle(new THREE.Vector3(-1, 0, 0), Math.PI / 2))
      object.quaternion.multiply(new THREE.Quaternion().setFromAxisAngle(new THREE.Vector3(0, 0, 1), Math.PI))
      object.updateMatrix();
      sceneObjects.push(object);
      scene.add(object);
    }
    // const box = new THREE.BoxHelper( object, 0xffff00 );
    // scene.add( box );
  });
}

function addSplines(splineColor, scene, trackDistance, eventPositionExtractor, replayData){
  const dronePosX = replayData['drone-px']
  const dronePosY = replayData['drone-py']
  const dronePosZ = replayData['drone-pz']
  const throttleInputs = replayData['input-t']
  const points = []
  const throttleInputPoints = []
  {
    for (let i = 0; i < dronePosX.length; i++) {
      if (dronePosX[i] === dronePosX[i - 1]
        && dronePosY[i] === dronePosY[i - 1]
        && dronePosZ[i] === dronePosZ[i - 1]) {
        continue
      }
      throttleInputPoints.push(throttleInputs[i])
      points.push(new THREE.Vector3(
        dronePosX[i],
        dronePosY[i],
        -dronePosZ[i]
      ));
    }
    const spline = new THREE.CatmullRomCurve3(points);
    // console.log(points.length, spline.getLength() * 2.5)
    const geometry = new THREE.TubeGeometry(spline, Math.round(spline.getLength() * 2.5), 0.2, 3, false)

    // Set Color gradient accross the spline
    let color1 = splineColor.color1;
    let color2 = splineColor.color2;
    let color1Copy = new THREE.Color();
    color1Copy.set(color1)
    const segmentSize = 100;
    const vertexSize = geometry.attributes.position.count
    const colors = new Float32Array(vertexSize * 3);

    let i = 0
    do {
      color1Copy.lerpHSL(color2, i / (vertexSize - 1));
      let j = 0
      do {
        colors[i * 3] = color1Copy.r
        colors[i * 3 + 1] = color1Copy.g
        colors[i * 3 + 2] = color1Copy.b
        i++
        j++
      } while (j < segmentSize && i < vertexSize)
      color1Copy.set(color1);
    } while (i < vertexSize)
    geometry.setAttribute('color', new THREE.Float32BufferAttribute(colors, 3));

    disposables.push(geometry);
    const material = new THREE.MeshStandardMaterial({
      transparent: true,
      depthTest: true,
      depthWrite: true,
      opacity: 0.8,
      side: THREE.DoubleSide,
      // color: 0x0ff00,
      wireframe: false,
      metalness: 0.5,
      vertexColors: true
    });
    disposables.push(material)
    const line = new THREE.Mesh(geometry, material);
    line.matrixAutoUpdate = false;
    sceneObjects.push(line);
    scene.add(line);
  }

  // THROTTLE!!
  {
    const throttleColor1 = new THREE.Color('yellow')
    const throttleColor2 = new THREE.Color('rgb(255,0,0)')
    const throttleMaterial = new THREE.MeshStandardMaterial({
      // transparent: true,
      // depthTest: true,
      // depthWrite: true,
      // opacity: 0.8,
      // side: THREE.DoubleSide,
      // color: throttleColor1,
      wireframe: false,
      // metalness: 0.5,
      vertexColors: true
    });
    disposables.push(throttleMaterial)
    // Calc max throttle input
    let maxThrottleInput = -Infinity
    for (let i = 0; i < throttleInputPoints.length; i++) {
      if (throttleInputPoints[i] > maxThrottleInput) {
        maxThrottleInput = throttleInputPoints[i];
      }
    }
    // I don't want to show not relevant mini blips or shady floating point stuff
    maxThrottleInput -= 0.001
    // console.log("max Throttle input: ", maxThrottleInput)

    // Create throttle splines
    for (let i = 0; i < throttleInputPoints.length; i++) {
      if (throttleInputPoints[i] < maxThrottleInput) {
        // Create spline for marking a lower throttle
        const throttleSplinePoints = []
        const throttleSplineInputs = []
        for (; i < throttleInputPoints.length && throttleInputPoints[i] < maxThrottleInput; i++) {
          throttleSplinePoints.push(points[i]);
          throttleSplineInputs.push(throttleInputPoints[i])
        }
        // Can't create spline with only 2 points
        if (throttleSplinePoints.length < 2){
          if (i + 1 < throttleInputPoints.length) {
            throttleSplinePoints.push(points[i + 1])
            throttleSplineInputs.push(throttleInputPoints[i + 1])
          } else {
            // no next point avaiable
            continue
          }
        }
        // console.log("createdThrottlePoints", throttleSplinePoints)
        const throttleCurve = new THREE.CatmullRomCurve3(throttleSplinePoints);
        const throttleGeometry = new THREE.TubeGeometry(throttleCurve, throttleSplinePoints.length, 0.25, 3, false)
        disposables.push(throttleGeometry)
        // Create color gradient for the strength of throttle decrease
        // console.log("throttleGeometry.attributes.position.count", throttleGeometry.attributes.position.count)
        // console.log(throttleGeometry.attributes.position.count / throttleSplinePoints.length)
        const vertexCount = throttleGeometry.attributes.position.count
        const segmentSize = vertexCount / throttleSplinePoints.length
        const color1Copy = new THREE.Color();
        color1Copy.set(throttleColor1)
        const colors = new Float32Array(vertexCount * 3);
        let i_2 = 0
        let currentSegment = 0
        do {
          // console.log(`(throttleIndex i / segmentSize): ${i_2} / ${segmentSize}: `, Math.floor(i_2 / segmentSize))
          // console.log('trottleInput', throttleSplineInputs[Math.floor(i_2 / segmentSize)]);
          // console.log('alpha value', 1 - (throttleSplineInputs[Math.floor(i_2 / segmentSize)] / 0.6))
          color1Copy.lerpHSL(throttleColor2, Math.min(1.1, Math.max(0, 1 - (throttleSplineInputs[Math.floor(i_2 / segmentSize)] / maxThrottleInput))));
          do {
            colors[i_2 * 3] = color1Copy.r
            colors[i_2 * 3 + 1] = color1Copy.g
            colors[i_2 * 3 + 2] = color1Copy.b
            i_2++
          } while (Math.floor(i_2 / segmentSize) <= currentSegment && i_2 < vertexCount)
          currentSegment++
          color1Copy.set(throttleColor1);
        } while (i_2 < vertexCount)
        throttleGeometry.setAttribute('color', new THREE.Float32BufferAttribute(colors, 3));

        const throttleLine = new THREE.Mesh(throttleGeometry, throttleMaterial);
        throttleLine.matrixAutoUpdate = false;
        sceneObjects.push(throttleLine);
        scene.add(throttleLine);
      }
    }
  }
}

function addTrackLabels(scene, checkpoints){
  // Add labels to the checkpoints
  checkpoints.forEach((checkpoint, index) => {
    const position = new THREE.Vector3(checkpoint.position.x, checkpoint.position.y, -checkpoint.position.z);
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

const basePanSpeed = 1
const baseDistance = 25

function animate(scene, camera) {
  if (cancelInProgress) {
    return;
  }
  // Update text label rotation
  textLabels.forEach(label => {
    label.lookAt(camera.position);
  });
  renderer.render(scene, camera);
  const currentDistance = camera.position.distanceTo(controls.target);
  controls.panSpeed = Math.max(0.8, Math.min(2, basePanSpeed * (baseDistance / currentDistance)));
  // console.log(controls.panSpeed)
  controls.update();
  requestAnimationFrameId = requestAnimationFrame(() => animate(scene, camera));
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
      // console.log("podium");
      // console.log(child)
      checkPoints.push({
        // name: child['name'].replace(/\$.*/, ''),
        name: 'podium-01',
        position: {
          x: child['local-position'][0],
          y: child['local-position'][1],
          z: child['local-position'][2]
        },
        // scaling: {
        //   x: 1,
        //   y: 1,
        //   z: 1
        // },
        scaling: {
          x: child['local-scale'][0],
          y: child['local-scale'][1],
          z: child['local-scale'][2]
        },
        // rotation: [0,0,0,0],
        rotation: [
          child['local-rotation'][0],
          child['local-rotation'][1],
          child['local-rotation'][2],
          child['local-rotation'][3]
        ],
        index: -1
      })
    } else if(child['gate-index'] != null
        && child['is-trigger'] === true
        && child['type'] === 301
    ) {
      // console.log('gate-index', child['gate-index']);
      // console.log('child', child);
      let checkpoint = {
        name: child['name'].replace(/\$.*/, ''),
        position: {
          x: child['local-position'][0],
          y: child['local-position'][1],
          z: child['local-position'][2]
        },
        scaling: {
          x: child['local-scale'][0], //*3.1, // scale up a bit to match the size of the checkpoints as close as possible
          y: child['local-scale'][1], //*3.1,
          z: child['local-scale'][2] //*1
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
        // console.log('module-scale?', child)
        checkpoint.name = getNameByModuleScale(checkpoint.name,
          child['module-scale'][0], child['module-scale'][1], child['module-scale'][2])
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
    'input-t': 'input-t',
    'drone-rpm0' : 'drone-rpm0',
    'drone-rpm1' : 'drone-rpm1',
    'drone-rpm2' : 'drone-rpm2',
    'drone-rpm3' : 'drone-rpm3',
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
  const inputT = markers['input-t'];
  const sumFloatArray = function (arr) {
    for (let i = 1; i < arr.length; i++) {
      arr[i] += arr[i - 1];
    }
  }
  sumFloatArray(time);
  sumFloatArray(dronePosX);
  sumFloatArray(dronePosY);
  sumFloatArray(dronePosZ);
  sumFloatArray(inputT)
}

let checkPoints = [];
let trackDetails;
const loadTrack = async (trackId) => {
  trackDetails = (await axios.get(
    `${process.env.DLAPP_PROTOCOL}://${window.location.hostname}${process.env.DLAPP_API_PORT}${process.env.DLAPP_API_PATH}/tracks/details/${trackId}`
  )).data;
  checkPoints = createCheckpointsFromTrackDetailData(trackDetails);

  // Calculate the center of the event positions
  // console.log("checkPoints", checkPoints)
  const center = checkPoints.reduce(
    (acc, event) => {
      const {x, y, z} = event.position;
      acc.x += x;
      acc.y += y;
      acc.z += -z;
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

const loadReplay = async (trackId, replayData, splineColor) => {
  await loadingTrackPromise;
  let replayUrl = replayData.replayUrl;
  // console.log("original replayUrl", replayUrl)
  // if replay url contains https://drl-game-dashboard.s3.amazonaws.com
  // then replace it with a proxy call because it has some crossorigin headers set
  if(replayData.replayUrl.startsWith("https://drl-game-dashboard.s3.amazonaws.com")) {
    let tempReplayUrl = replayUrl;
    // TODO Doesn't work on local env currently, because we need a seperate nginx port for that and it needs to be running
    replayUrl = `${process.env.DLAPP_PROTOCOL}://${window.location.hostname}${process.env.DLAPP_API_PORT}/proxy?url=${encodeURIComponent(tempReplayUrl)}`;
    // console.log("corrected replayUrl", replayUrl)
  }

  const response = await axios.get(replayUrl, { responseType: 'arraybuffer' });
  const decodedData = new TextDecoder().decode(response.data);
  const replayJson = extractJSON(decodedData)[0];
  let markers = findMarkersAndExtractData(new Uint8Array(response.data));
  // console.log("initial markers", markers)
  sumMarkerData(markers);
  // console.log("sum", markers)
  // console.log(trackDetails)
  addSplines(splineColor, scene, trackDetails['map-distance'] * trackDetails['map-laps'],
    (event) => event["event-position"],
    markers
  )
  // addReplayLabels(scene, replayCheckPointData.events, (event) => event["event-position"])
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

const loadAsset = async function(asset){
  return
}

// const assetLoader = {
//   "gate-drl-34-b": {
//     "lod": async () => (await import('assets/gate_triggers/gate-drl-34-b-lod2.obj?url')).default,
//     "trigger": async () => (await import('assets/gate_triggers/gate-drl-34-b-trigger.obj?url')).default,
//   }
// }

onMounted(async () => {
  renderer = new THREE.WebGLRenderer();
  renderer.setSize(container.value.offsetWidth, container.value.offsetHeight);
  renderer.setPixelRatio( window.devicePixelRatio );
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
  controls.rotateSpeed = 0.5
  controls.zoomSpeed = 1.8
  // controls.minDistance = 5
  controls.minDistance = 0.1
  controls.maxDistance = 1000
  controls.zoomToCursor = true
  // controls.mouseButtons = {
  //   LEFT: THREE.MOUSE.ROTATE,
  //   MIDDLE: THREE.MOUSE.PAN,
  //   RIGHT: THREE.MOUSE.PAN
  // }
  // controls.screenSpacePanning = false

  const ambientLight = new THREE.AmbientLight(0xFFFFFF, 0.5); // Add ambient light with an intensity of 1
  scene.add(ambientLight);

  // Create a directional light
  const directionalLight = new THREE.DirectionalLight(0xffffff, 0.5);
  directionalLight.position.set(1, 1, 1);
  scene.add(directionalLight);

  // const axesHelper = new THREE.AxesHelper( 5 );
  // scene.add( axesHelper );

  // TODO FINISH RACER4 implementation
  // loadRacer4().then((obj) => {
  //   // obj.position.set(1,1,1)
  //   sceneObjects.push(obj)
  //   scene.add(obj)
  // })

  animate(scene, camera);
});

const loadRacer4 = async function () {
  const r4DroneParts = [
    "antenna-04",
    // "attachment-02",
    // "esc-02",
    "frame-34-body-arm-0",
    "frame-34-body-arm-1",
    "frame-34-body-arm-2",
    "frame-34-body-arm-3",
    "frame-34-body",
    "frame-34-canopy-b-arm-0",
    "frame-34-canopy-b-arm-1",
    "frame-34-canopy-b-arm-2",
    "frame-34-canopy-b-arm-3",
    "frame-34-canopy-body-b",
    "frame-34-canopy-body-t",
    "frame-34-canopy-glass",
    "frame-34-canopy-t-arm-0",
    "frame-34-canopy-t-arm-1",
    "frame-34-canopy-t-arm-2",
    "frame-34-canopy-t-arm-3",
    "frame-34-part-cradle",
    "frame-34-part-leds",
    "frame-34-part-tail",
    "propeller-45-0",
    "propeller-45-1",
    "propeller-45-2",
    "propeller-45-3",
    "motor-30-base-0",
    "motor-30-base-1",
    "motor-30-base-2",
    "motor-30-base-3",
    "motor-30-cap-0",
    "motor-30-cap-1",
    "motor-30-cap-2",
    "motor-30-cap-3",
  ]
  const group = new THREE.Group()
  for (const r4DronePart of r4DroneParts) {
    const obj = await assetObjLoader(await assetLoader[r4DronePart].lod())
    if(assetLoader[r4DronePart].offset){
      assetLoader[r4DronePart].offset(obj)
    }
    group.add(obj)
  }
  return group
}

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
