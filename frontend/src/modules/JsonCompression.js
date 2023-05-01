import pako from 'pako';

function compressJSON(json) {
  const jsonString = JSON.stringify(json);
  const compressedData = pako.deflate(jsonString, {level: 9});
  const base64Encoded = btoa(String.fromCharCode(...compressedData));
  return base64Encoded;
}

function decompressJSON(base64Encoded) {
  const compressedData = Uint8Array.from(atob(base64Encoded), c => c.charCodeAt(0));
  const jsonString = pako.inflate(compressedData, {to: 'string'});
  const json = JSON.parse(jsonString);
  return json;
}

export {compressJSON, decompressJSON};
