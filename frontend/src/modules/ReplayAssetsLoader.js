import {OBJLoader} from "three/addons/loaders/OBJLoader.js";

const assetLoader = {
  "podium-01": {
    "lod": async () => (await import('assets/replay_assets/podiums/podium-01-base-lod2.obj?url')).default,
  },
  "gate-empty-01" : {
    "trigger": async () => (await import('assets/replay_assets/gates/gate-empty-01.obj?url')).default,
  },
  "layout-orient-1": {
    "refs": ["gate-empty-01"]
  },
  "layout-orient-2": {
    "refs": ["gate-empty-01"]
  },
  "layout-orient-3": {
    "refs": ["gate-empty-01"]
  },
  "layout-orient-4": {
    "refs": ["gate-empty-01"]
  },
  "layout-orient-5": {
    "refs": ["gate-empty-01"]
  },
  "gate-empty-02" : {
    "trigger": async () => (await import('assets/replay_assets/gates/gate-empty-02.obj?url')).default,
  },
  "gate-empty-03" : {
    "trigger": async () => (await import('assets/replay_assets/gates/gate-empty-03.obj?url')).default,
  },
  "gate-empty-04" : {
    "trigger": async () => (await import('assets/replay_assets/gates/gate-empty-04.obj?url')).default,
  },
  "gate-empty-05" : {
    "trigger": async () => (await import('assets/replay_assets/gates/gate-empty-05.obj?url')).default,
  },
  "gate-drl-01" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-01-trigger.obj?url')).default,
  },
  "gate-drl-02" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-02-trigger.obj?url')).default,
  },
  "gate-drl-03" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-03-lod2.obj?url')).default,
  },
  "gate-drl-04" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-04-lod2.obj?url')).default,
  },
  "gate-drl-05" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-05-lod2.obj?url')).default,
  },
  "gate-drl-06" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-06-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-06-trigger.obj?url')).default,
  },
  "gate-drl-07" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-07-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-07-trigger-01.obj?url')).default,
  },
  "gate-drl-08" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-08-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-08-trigger-01.obj?url')).default,
  },
  "gate-drl-09" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-09-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-09-trigger-01.obj?url')).default,
  },
  "gate-drl-10" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-10-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-10-trigger-01.obj?url')).default,
  },
  "gate-drl-11" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-11-lod2.obj?url')).default,
  },
  "gate-drl-12" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-12-lod2.obj?url')).default,
  },
  "gate-drl-13" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-13-lod2.obj?url')).default,
  },
  "gate-drl-14-base" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-14-base-lod2.obj?url')).default,
  },
  "gate-drl-15" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-15-lod2.obj?url')).default,
  },
  "gate-drl-16" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-16-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-16-trigger.obj?url')).default,
  },
  "gate-drl-17" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-17-lod2.obj?url')).default,
  },
  "gate-drl-18" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-18-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-18-trigger.obj?url')).default,
  },
  "gate-drl-19" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-19-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-19-trigger.obj?url')).default,
  },
  "gate-drl-20" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-20-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-20-trigger.obj?url')).default,
  },
  "gate-drl-21" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-21-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-21-trigger-01.obj?url')).default,
  },
  "gate-drl-22-a" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-22-a-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-22-a-trigger.obj?url')).default,
  },
  "gate-drl-22-b" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-22-b-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-22-b-trigger.obj?url')).default,
  },
  "gate-drl-22-c" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-22-c-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-22-c-trigger.obj?url')).default,
  },
  "gate-drl-22-d" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-22-d-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-22-d-trigger.obj?url')).default,
  },
  "gate-drl-22-e" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-22-e-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-22-e-trigger.obj?url')).default,
  },
  "gate-drl-22-f" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-22-f-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-22-f-trigger.obj?url')).default,
  },
  "gate-drl-23-a" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-23-a-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-23-a-trigger.obj?url')).default,
  },
  "gate-drl-23-b" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-23-b-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-23-b-trigger.obj?url')).default,
  },
  "gate-drl-23-c" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-23-c-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-23-c-trigger.obj?url')).default,
  },
  "gate-drl-23-d" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-23-d-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-23-d-trigger.obj?url')).default,
  },
  "gate-drl-23-e" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-23-e-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-23-e-trigger.obj?url')).default,
  },
  "gate-drl-23-f" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-23-f-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-23-f-trigger.obj?url')).default,
  },
  "gate-drl-24-a" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-24-a-lod2.obj?url')).default,
  },
  "gate-drl-24-b" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-24-b-lod2.obj?url')).default,
  },
  "gate-drl-24-c" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-24-c-lod2.obj?url')).default,
  },
  "gate-drl-25": {
    "refs": ["gate-drl-25-a"]
  },
  "gate-drl-25-a-base" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-25-a-base-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-25-a-base-trigger.obj?url')).default,
  },
  "gate-drl-25-a" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-25-a-lod2.obj?url')).default,
    "refs": ["gate-drl-25-a-base"]
  },
  "gate-drl-25-b-base" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-25-b-base-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-25-b-base-trigger.obj?url')).default,
  },
  "gate-drl-25-b" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-25-b-lod2.obj?url')).default,
  },
  "gate-drl-26": {
    "refs": ["gate-drl-25-b-base", "gate-drl-25-b"]
  },
  "gate-drl-25-c-base" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-25-c-base-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-25-c-base-trigger.obj?url')).default,
  },
  "gate-drl-25-c" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-25-c-lod2.obj?url')).default,
  },
  "gate-drl-25-d" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-25-d-lod2.obj?url')).default,
  },
  "gate-drl-27" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-27-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-27-trigger.obj?url')).default,
  },
  "gate-drl-28-a" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-28-a-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-28-a-trigger.obj?url')).default,
  },
  "gate-drl-28-b" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-28-b-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-28-b-trigger.obj?url')).default,
  },
  "gate-drl-29" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-29-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-29-trigger-01.obj?url')).default,
  },
  "gate-drl-30" : {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-30-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-30-trigger.obj?url')).default,
  },
  "gate-drl-31-a": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-31-a-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-31-a-trigger.obj?url')).default,
  },
  "gate-drl-31": {
    "refs": ["gate-drl-31-a"]
  },
  "gate-drl-31-a-drl": {
    "refs": ["gate-drl-31-a"]
  },
  "gate-drl-31-a-al": {
    "refs": ["gate-drl-31-a"]
  },
  "gate-drl-31-a-df": {
    "refs": ["gate-drl-31-a"]
  },
  "gate-drl-31-a-df-a": {
    "refs": ["gate-drl-31-a"]
  },
  "gate-drl-31-a-df-b": {
    "refs": ["gate-drl-31-a"]
  },
  "gate-drl-31-a-dk": {
    "refs": ["gate-drl-31-a"]
  },
  "gate-drl-31-a-gc": {
    "refs": ["gate-drl-31-a"]
  },
  "gate-drl-31-a-pk": {
    "refs": ["gate-drl-31-a"]
  },
  "gate-drl-31-a-tm": {
    "refs": ["gate-drl-31-a"]
  },
  "gate-drl-31-a-us": {
    "refs": ["gate-drl-31-a"]
  },
  "gate-drl-31-b": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-31-b-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-31-b-trigger.obj?url')).default,
  },
  "gate-drl-31-b-drl": {
    refs: ["gate-drl-31-b"]
  },
  "gate-drl-31-b-al": {
    "refs": ["gate-drl-31-b"]
  },
  "gate-drl-31-b-df": {
    "refs": ["gate-drl-31-b"]
  },
  "gate-drl-31-b-df-a": {
    "refs": ["gate-drl-31-b"]
  },
  "gate-drl-31-b-df-b": {
    "refs": ["gate-drl-31-b"]
  },
  "gate-drl-31-b-dk": {
    "refs": ["gate-drl-31-b"]
  },
  "gate-drl-31-b-gc": {
    "refs": ["gate-drl-31-b"]
  },
  "gate-drl-31-b-pk": {
    "refs": ["gate-drl-31-b"]
  },
  "gate-drl-31-b-tm": {
    "refs": ["gate-drl-31-b"]
  },
  "gate-drl-31-b-us": {
    "refs": ["gate-drl-31-b"]
  },
  "gate-drl-32-a": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-32-a-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-32-a-trigger.obj?url')).default,
  },
  "gate-drl-32": {
    "refs": ["gate-drl-32-a"]
  },
  "gate-drl-32-a-drl": {
    "refs": ["gate-drl-32-a"]
  },
  "gate-drl-32-a-al": {
    "refs": ["gate-drl-32-a"]
  },
  "gate-drl-32-a-df": {
    "refs": ["gate-drl-32-a"]
  },
  "gate-drl-32-a-df-a": {
    "refs": ["gate-drl-32-a"]
  },
  "gate-drl-32-a-df-b": {
    "refs": ["gate-drl-32-a"]
  },
  "gate-drl-32-a-dk": {
    "refs": ["gate-drl-32-a"]
  },
  "gate-drl-32-a-gc": {
    "refs": ["gate-drl-32-a"]
  },
  "gate-drl-32-a-pk": {
    "refs": ["gate-drl-32-a"]
  },
  "gate-drl-32-a-tm": {
    "refs": ["gate-drl-32-a"]
  },
  "gate-drl-32-a-us": {
    "refs": ["gate-drl-32-a"]
  },
  "gate-drl-32-b": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-32-b-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-32-b-trigger.obj?url')).default,
  },
  "gate-drl-32-b-drl": {
    "refs": ["gate-drl-32-b"]
  },
  "gate-drl-32-b-al": {
    "refs": ["gate-drl-32-b"]
  },
  "gate-drl-32-b-df": {
    "refs": ["gate-drl-32-b"]
  },
  "gate-drl-32-b-df-a": {
    "refs": ["gate-drl-32-b"]
  },
  "gate-drl-32-b-df-b": {
    "refs": ["gate-drl-32-b"]
  },
  "gate-drl-32-b-dk": {
    "refs": ["gate-drl-32-b"]
  },
  "gate-drl-32-b-gc": {
    "refs": ["gate-drl-32-b"]
  },
  "gate-drl-32-b-pk": {
    "refs": ["gate-drl-32-b"]
  },
  "gate-drl-32-b-tm": {
    "refs": ["gate-drl-32-b"]
  },
  "gate-drl-32-b-us": {
    "refs": ["gate-drl-32-b"]
  },
  "gate-drl-33-a": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-33-a-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-33-a-trigger.obj?url')).default,
  },
  "gate-drl-33": {
    "refs": ["gate-drl-33-a"]
  },
  "gate-drl-33-a-drl": {
    "refs": ["gate-drl-33-a"]
  },
  "gate-drl-33-a-al": {
    "refs": ["gate-drl-33-a"]
  },
  "gate-drl-33-a-df": {
    "refs": ["gate-drl-33-a"]
  },
  "gate-drl-33-a-df-a": {
    "refs": ["gate-drl-33-a"]
  },
  "gate-drl-33-a-df-b": {
    "refs": ["gate-drl-33-a"]
  },
  "gate-drl-33-a-dk": {
    "refs": ["gate-drl-33-a"]
  },
  "gate-drl-33-a-gc": {
    "refs": ["gate-drl-33-a"]
  },
  "gate-drl-33-a-pk": {
    "refs": ["gate-drl-33-a"]
  },
  "gate-drl-33-a-tm": {
    "refs": ["gate-drl-33-a"]
  },
  "gate-drl-33-a-us": {
    "refs": ["gate-drl-33-a"]
  },
  "gate-drl-33-b": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-33-b-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-33-b-trigger.obj?url')).default,
  },
  "gate-drl-33-b-drl": {
    "refs": ["gate-drl-33-b"]
  },
  "gate-drl-33-b-al": {
    "refs": ["gate-drl-33-b"]
  },
  "gate-drl-33-b-df": {
    "refs": ["gate-drl-33-b"]
  },
  "gate-drl-33-b-df-a": {
    "refs": ["gate-drl-33-b"]
  },
  "gate-drl-33-b-df-b": {
    "refs": ["gate-drl-33-b"]
  },
  "gate-drl-33-b-dk": {
    "refs": ["gate-drl-33-b"]
  },
  "gate-drl-33-b-gc": {
    "refs": ["gate-drl-33-b"]
  },
  "gate-drl-33-b-pk": {
    "refs": ["gate-drl-33-b"]
  },
  "gate-drl-33-b-tm": {
    "refs": ["gate-drl-33-b"]
  },
  "gate-drl-33-b-us": {
    "refs": ["gate-drl-33-b"]
  },
  "gate-drl-34-a": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-34-a-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-34-a-trigger.obj?url')).default,
  },
  "gate-drl-34": {
    "refs": ["gate-drl-34-a"]
  },
  "gate-drl-34-a-drl": {
    "refs": ["gate-drl-34-a"]
  },
  "gate-drl-34-a-al": {
    "refs": ["gate-drl-34-a"]
  },
  "gate-drl-34-a-df": {
    "refs": ["gate-drl-34-a"]
  },
  "gate-drl-34-a-df-a": {
    "refs": ["gate-drl-34-a"]
  },
  "gate-drl-34-a-df-b": {
    "refs": ["gate-drl-34-a"]
  },
  "gate-drl-34-a-dk": {
    "refs": ["gate-drl-34-a"]
  },
  "gate-drl-34-a-gc": {
    "refs": ["gate-drl-34-a"]
  },
  "gate-drl-34-a-pk": {
    "refs": ["gate-drl-34-a"]
  },
  "gate-drl-34-a-tm": {
    "refs": ["gate-drl-34-a"]
  },
  "gate-drl-34-a-us": {
    "refs": ["gate-drl-34-a"]
  },
  "gate-drl-34-b": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-34-b-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-34-b-trigger.obj?url')).default,
  },
  "gate-drl-34-b-drl": {
    "refs": ["gate-drl-34-b"]
  },
  "gate-drl-34-b-al": {
    "refs": ["gate-drl-34-b"]
  },
  "gate-drl-34-b-df": {
    "refs": ["gate-drl-34-b"]
  },
  "gate-drl-34-b-df-a": {
    "refs": ["gate-drl-34-b"]
  },
  "gate-drl-34-b-df-b": {
    "refs": ["gate-drl-34-b"]
  },
  "gate-drl-34-b-dk": {
    "refs": ["gate-drl-34-b"]
  },
  "gate-drl-34-b-gc": {
    "refs": ["gate-drl-34-b"]
  },
  "gate-drl-34-b-pk": {
    "refs": ["gate-drl-34-b"]
  },
  "gate-drl-34-b-tm": {
    "refs": ["gate-drl-34-b"]
  },
  "gate-drl-34-b-us": {
    "refs": ["gate-drl-34-b"]
  },
  "gate-drl-35": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-35-lod2.obj?url')).default,
  },
  "gate-drl-36": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-36-lod2.obj?url')).default,
  },
  "gate-drl-37": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-37-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-37-trigger.obj?url')).default,
  },
  "gate-drl-38": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-38-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-38-trigger.obj?url')).default,
  },
  "gate-drl-39": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-39-lod2.obj?url')).default,
    "refs": ["gate-drl-38"]
  },
  "gate-drl-39-a": {
    "refs": ["gate-drl-39"]
  },
  "gate-drl-39-b": {
    "refs": ["gate-drl-39"]
  },
  "gate-drl-40": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-40-lod2.obj?url')).default,
  },
  "gate-drl-41": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-41-lod2.obj?url')).default,
  },
  "gate-drl-44": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-44-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-44-trigger.obj?url')).default,
  },
  "gate-drl-45-eagle-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-45-eagle-01-lod2.obj?url')).default,
  },
  "gate-drl-45-eagle-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-45-eagle-02-lod2.obj?url')).default,
  },
  "gate-drl-45-eagle-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-45-eagle-03-lod2.obj?url')).default,
  },
  "gate-drl-45-eagle-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-45-eagle-04-lod2.obj?url')).default,
  },
  "gate-drl-45": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-45-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-45-trigger.obj?url')).default,
  },
  "gate-drl-46-glass": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-46-glass-lod2.obj?url')).default,
  },
  "gate-drl-46": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-46-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-46-trigger.obj?url')).default,
  },
  "gate-drl-47-eagle-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-47-eagle-01-lod2.obj?url')).default,
  },
  "gate-drl-47-eagle-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-47-eagle-02-lod2.obj?url')).default,
  },
  "gate-drl-47-eagle-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-47-eagle-03-lod2.obj?url')).default,
  },
  "gate-drl-47": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-47-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-47-trigger.obj?url')).default,
  },
  "gate-drl-48": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-48-lod2.obj?url')).default,
  },
  "gate-drl-49": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-49-lod2.obj?url')).default,
  },
  "gate-drl-50": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-50-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-drl-50-trigger.obj?url')).default,
  },
  "gate-drl-51": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-51-lod2.obj?url')).default,
  },
  "gate-drl-52": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-drl-52-lod2.obj?url')).default,
  },

  // HALLOWEEN !
  "gate-halloween-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-halloween-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-halloween-01-trigger.obj?url')).default,
  },
  "gate-halloween-01-a": {
    "refs": ["gate-halloween-01"]
  },
  "gate-halloween-01-b": {
    "refs": ["gate-halloween-01"]
  },
  "gate-halloween-01-c": {
    "refs": ["gate-halloween-01"]
  },
  "gate-halloween-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-halloween-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-halloween-02-trigger.obj?url')).default,
  },
  "gate-halloween-02-a": {
    "refs": ["gate-halloween-02"]
  },
  "gate-halloween-02-b": {
    "refs": ["gate-halloween-02"]
  },
  "gate-halloween-02-c": {
    "refs": ["gate-halloween-02"]
  },

  // INFLATEABLE !
  "gate-inflatable-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-inflatable-01-lod2.obj?url')).default,
  },
  "gate-inflatable-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-inflatable-02-lod2.obj?url')).default,
  },
  "gate-inflatable-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-inflatable-03-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-inflatable-03-trigger.obj?url')).default,
  },
  "gate-inflatable-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-inflatable-04-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-inflatable-04-trigger.obj?url')).default,
  },
  "gate-inflatable-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-inflatable-05-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-inflatable-05-trigger.obj?url')).default,
  },
  "gate-inflatable-06": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-inflatable-06-lod2.obj?url')).default,
  },
  "gate-inflatable-07": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-inflatable-07-lod2.obj?url')).default,
  },

  // MISSION GATES
  "gate-missions-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-missions-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-missions-01-trigger.obj?url')).default,
  },
  "gate-missions-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-missions-02-lod2.obj?url')).default,
  },
  "gate-missions-03-a": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-missions-03-a-lod2.obj?url')).default,
  },
  "gate-missions-03-b": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-missions-03-b-lod2.obj?url')).default,
  },
  "gate-missions-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-missions-04-lod2.obj?url')).default,
  },
  "gate-missions-05-a": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-missions-05-a-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-missions-05-a-trigger.obj?url')).default,
  },
  "gate-missions-05-b": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-missions-05-b-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-missions-05-b-trigger.obj?url')).default,
  },
  "gate-missions-06-a": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-missions-06-a-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-missions-06-a-trigger.obj?url')).default,
  },
  "gate-missions-06-b": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-missions-06-b-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-missions-06-b-trigger.obj?url')).default,
  },
  "gate-missions-07-a": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-missions-07-a-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-missions-07-a-trigger.obj?url')).default,
  },
  "gate-missions-07-b": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-missions-07-b-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-missions-07-b-trigger.obj?url')).default,
  },
  "gate-missions-08": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-missions-08-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-missions-08-trigger.obj?url')).default,
  },

  // MUTLI GP!
  "gate-multigp-a-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-a-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-multigp-a-01-trigger.obj?url')).default,
  },
  "gate-multigp-01": {
    "refs": ["gate-multigp-b-01"]
  },
  "gate-multigp-a-01-drl": {
    "refs": ["gate-multigp-a-01"]
  },
  "gate-multigp-a-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-a-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-multigp-a-02-trigger.obj?url')).default,
  },
  "gate-multigp-a-02-drl": {
    "refs": ["gate-multigp-a-02"]
  },
  "gate-multigp-a-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-a-03-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-multigp-a-03-trigger.obj?url')).default,
  },
  "gate-multigp-a-03-drl": {
    "refs": ["gate-multigp-a-03"]
  },
  "gate-multigp-a-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-a-04-lod2.obj?url')).default,
  },
  "gate-multigp-a-04-drl": {
    "refs": ["gate-multigp-a-04"]
  },
  "gate-multigp-a-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-a-05-lod2.obj?url')).default,
  },
  "gate-multigp-a-05-drl": {
    "refs": ["gate-multigp-a-05"]
  },
  "gate-multigp-a-06": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-a-06-lod2.obj?url')).default,
  },
  "gate-multigp-a-06-drl": {
    "refs": ["gate-multigp-a-06"]
  },
  "gate-multigp-b-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-b-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-multigp-b-01-trigger.obj?url')).default,
  },
  "gate-multigp-b-01-drl": {
    "refs": ["gate-multigp-b-01"]
  },
  "gate-multigp-b-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-b-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-multigp-b-02-trigger.obj?url')).default,
  },
  "gate-multigp-b-02-drl": {
    "refs": ["gate-multigp-b-02"]
  },
  "gate-multigp-b-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-b-03-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-multigp-b-03-trigger.obj?url')).default,
  },
  "gate-multigp-b-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-b-04-lod2.obj?url')).default,
  },
  "gate-multigp-b-04-drl": {
    "refs": ["gate-multigp-b-04"]
  },
  "gate-multigp-b-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-b-05-lod2.obj?url')).default,
  },
  "gate-multigp-b-05-drl": {
    "refs": ["gate-multigp-b-05"]
  },
  "gate-multigp-b-06": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-b-06-lod2.obj?url')).default,
  },
  "gate-multigp-b-06-drl": {
    "refs": ["gate-multigp-b-06"]
  },
  "gate-multigp-b-07": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-b-07-lod2.obj?url')).default,
  },
  "gate-multigp-b-07-drl": {
    "refs": ["gate-multigp-b-07"]
  },
  "gate-multigp-b-08": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-b-08-lod2.obj?url')).default,
  },
  "gate-multigp-b-08-drl": {
    "refs": ["gate-multigp-b-08"]
  },
  "gate-multigp-c-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-c-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-multigp-c-01-trigger.obj?url')).default,
  },
  "gate-multigp-c-01-drl": {
    "refs": ["gate-multigp-c-01"]
  },
  "gate-multigp-c-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-c-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-multigp-c-02-trigger.obj?url')).default,
  },
  "gate-multigp-c-02-drl": {
    "refs": ["gate-multigp-c-02"]
  },
  "gate-multigp-c-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-c-03-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-multigp-c-03-trigger.obj?url')).default,
  },
  "gate-multigp-c-03-drl": {
    "refs": ["gate-multigp-c-03"]
  },
  "gate-multigp-c-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-c-04-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-multigp-c-04-trigger.obj?url')).default,
  },
  "gate-multigp-c-04-drl": {
    "refs": ["gate-multigp-c-04"]
  },
  "gate-multigp-c-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-c-05-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-multigp-c-05-trigger.obj?url')).default,
  },
  "gate-multigp-c-05-drl": {
    "refs": ["gate-multigp-c-05"]
  },
  "gate-multigp-c-06": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-c-06-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-multigp-c-06-trigger.obj?url')).default,
  },
  "gate-multigp-c-06-drl": {
    "refs": ["gate-multigp-c-06"]
  },
  "gate-multigp-d-01-cloth": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-d-01-cloth-lod2.obj?url')).default,
  },
  "gate-multigp-d-01-frame": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-d-01-frame-lod2.obj?url')).default,
  },
  "gate-multigp-e-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-e-01-lod2.obj?url')).default,
  },
  "gate-multigp-e-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-multigp-e-02-lod2.obj?url')).default,
  },
  // NEON GATES
  "gate-neon-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-neon-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-neon-01-trigger.obj?url')).default,
  },
  "gate-neon-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-neon-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-neon-02-trigger.obj?url')).default,
  },
  "gate-neon-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-neon-03-lod2.obj?url')).default,
  },
  "gate-neon-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-neon-04-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-neon-04-trigger.obj?url')).default,
  },
  "gate-neon-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-neon-05-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-neon-05-trigger.obj?url')).default,
  },
  "gate-neon-06": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-neon-06-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-neon-06-trigger.obj?url')).default,
  },
  "gate-neon-07": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-neon-07-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-neon-07-trigger.obj?url')).default,
  },
  "gate-neon-08": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-neon-08-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-neon-08-trigger.obj?url')).default,
  },
  "gate-neon-09": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-neon-09-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-neon-09-trigger.obj?url')).default,
  },
  "gate-neon-10": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-neon-10-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-neon-10-trigger.obj?url')).default,
  },
  // OTHER GATES
  "gate-other-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-other-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-other-01-trigger.obj?url')).default,
  },
  "gate-others-01": {
    "refs": ["gate-other-01"]
  },
  "gate-other-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-other-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-other-02-trigger.obj?url')).default,
  },
  "gate-others-02": {
    "refs": ["gate-other-02"]
  },
  "gate-other-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-other-03-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-other-03-trigger.obj?url')).default,
  },
  "gate-others-03": {
    "refs": ["gate-other-03"]
  },
  "gate-other-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-other-04-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-other-04-trigger.obj?url')).default,
  },
  "gate-others-04": {
    "refs": ["gate-other-04"]
  },
  "gate-other-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-other-05-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-other-05-trigger.obj?url')).default,
  },
  "gate-others-05": {
    "refs": ["gate-other-05"]
  },
  // REGIONAL GATES
  "gate-regional-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-regional-01-lod2.obj?url')).default,
  },
  "gate-regional-01-a": {
    "refs": ["gate-regional-01"]
  },
  "gate-regional-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-regional-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-regional-02-trigger.obj?url')).default,
  },
  "gate-regional-02-a": {
    "refs": ["gate-regional-02"]
  },
  "gate-regional-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-regional-03-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-regional-03-trigger.obj?url')).default,
  },
  "gate-regional-03-a": {
    "refs": ["gate-regional-03"]
  },
  "gate-regional-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-regional-04-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-regional-04-trigger.obj?url')).default,
  },
  "gate-regional-04-a": {
    "refs": ["gate-regional-04"]
  },
  "gate-regional-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-regional-05-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-regional-05-trigger.obj?url')).default,
  },
  "gate-regional-05-a": {
    "refs": ["gate-regional-05"]
  },
  "gate-swatch-inflatable-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-swatch-inflatable-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-swatch-inflatable-01-trigger.obj?url')).default,
  },
  "gate-swatch-inflatable-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-swatch-inflatable-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-swatch-inflatable-02-trigger.obj?url')).default,
  },
  "gate-swatch-inflatable-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-swatch-inflatable-03-lod2.obj?url')).default,
  },
  "gate-swatch-inflatable-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-swatch-inflatable-04-lod2.obj?url')).default,
  },
  "gate-swatch-inflatable-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-swatch-inflatable-05-lod2.obj?url')).default,
  },

  // Alphabetic Gates
  "gate-a-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-a-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-a-01-trigger.obj?url')).default,
  },
  "gate-a-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-a-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-a-02-trigger.obj?url')).default,
  },
  "gate-a-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-a-03-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-a-03-trigger.obj?url')).default,
  },
  "gate-a-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-a-04-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-a-04-trigger.obj?url')).default,
  },
  "gate-a-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-a-05-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-a-05-trigger.obj?url')).default,
  },
  "gate-a-06": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-a-06-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-a-06-trigger.obj?url')).default,
  },
  "gate-b-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-b-01-lod2.obj?url')).default,
  },
  "gate-b-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-b-02-lod2.obj?url')).default,
  },
  "gate-b-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-b-03-lod2.obj?url')).default,
  },
  "gate-b-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-b-04-lod2.obj?url')).default,
  },
  "gate-b-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-b-05-lod2.obj?url')).default,
  },
  "gate-b-06": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-b-06-lod2.obj?url')).default,
  },
  "gate-c-01-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-01-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-01-01-trigger.obj?url')).default,
  },
  "gate-c-01-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-01-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-01-02-trigger.obj?url')).default,
  },
  "gate-c-01-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-01-03-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-01-03-trigger.obj?url')).default,
  },
  "gate-c-01-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-01-04-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-01-04-trigger.obj?url')).default,
  },
  "gate-c-01-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-01-05-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-01-05-trigger.obj?url')).default,
  },
  "gate-c-01-06": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-01-06-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-01-06-trigger.obj?url')).default,
  },
  "gate-c-02-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-02-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-02-01-trigger.obj?url')).default,
  },
  "gate-c-02-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-02-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-02-02-trigger.obj?url')).default,
  },
  "gate-c-02-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-02-03-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-02-03-trigger.obj?url')).default,
  },
  "gate-c-02-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-02-04-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-02-04-trigger.obj?url')).default,
  },
  "gate-c-02-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-02-05-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-02-05-trigger.obj?url')).default,
  },
  "gate-c-02-06": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-02-06-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-02-06-trigger.obj?url')).default,
  },
  "gate-c-03-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-03-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-03-01-trigger.obj?url')).default,
  },
  "gate-c-03-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-03-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-03-02-trigger.obj?url')).default,
  },
  "gate-c-03-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-03-03-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-03-03-trigger.obj?url')).default,
  },
  "gate-c-03-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-03-04-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-03-04-trigger.obj?url')).default,
  },
  "gate-c-03-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-03-05-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-03-05-trigger.obj?url')).default,
  },
  "gate-c-03-06": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-03-06-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-03-06-trigger.obj?url')).default,
  },
  "gate-c-04-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-04-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-04-01-trigger.obj?url')).default,
  },
  "gate-c-04-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-04-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-04-02-trigger.obj?url')).default,
  },
  "gate-c-04-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-04-03-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-04-03-trigger.obj?url')).default,
  },
  "gate-c-04-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-04-04-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-04-04-trigger.obj?url')).default,
  },
  "gate-c-04-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-04-05-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-04-05-trigger.obj?url')).default,
  },
  "gate-c-04-06": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-04-06-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-04-06-trigger.obj?url')).default,
  },
  "gate-c-05-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-05-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-05-01-trigger.obj?url')).default,
  },
  "gate-c-05-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-05-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-05-02-trigger.obj?url')).default,
  },
  "gate-c-05-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-05-03-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-05-03-trigger.obj?url')).default,
  },
  "gate-c-05-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-05-04-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-05-04-trigger.obj?url')).default,
  },
  "gate-c-05-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-05-05-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-05-05-trigger.obj?url')).default,
  },
  "gate-c-05-06": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-05-06-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-05-06-trigger.obj?url')).default,
  },
  "gate-c-06-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-06-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-06-01-trigger.obj?url')).default,
  },
  "gate-c-06-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-06-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-06-02-trigger.obj?url')).default,
  },
  "gate-c-06-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-06-03-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-06-03-trigger.obj?url')).default,
  },
  "gate-c-06-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-06-04-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-06-04-trigger.obj?url')).default,
  },
  "gate-c-06-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-06-05-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-06-05-trigger.obj?url')).default,
  },
  "gate-c-06-06": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-c-06-06-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-c-06-06-trigger.obj?url')).default,
  },
  "gate-d-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-d-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-d-01-trigger.obj?url')).default,
  },
  "gate-d-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-d-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-d-02-trigger.obj?url')).default,
  },
  "gate-d-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-d-03-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-d-03-trigger.obj?url')).default,
  },
  "gate-d-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-d-04-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-d-04-trigger.obj?url')).default,
  },
  "gate-d-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-d-05-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-d-05-trigger.obj?url')).default,
  },
  "gate-d-06": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-d-06-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-d-06-trigger.obj?url')).default,
  },
  // Gates Tryouts (Dronepark modules)
  "gate-tryouts-a-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-a-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-a-01-trigger.obj?url')).default,
  },
  "gate-tryouts-a-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-a-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-a-02-trigger.obj?url')).default,
  },
  "gate-tryouts-a-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-a-03-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-a-03-trigger.obj?url')).default,
  },
  "gate-tryouts-a-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-a-04-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-a-04-trigger.obj?url')).default,
  },
  "gate-tryouts-a-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-a-05-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-a-05-trigger.obj?url')).default,
  },
  "gate-tryouts-a-06": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-a-06-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-a-06-trigger.obj?url')).default,
  },
  "gate-tryouts-b-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-b-01-lod2.obj?url')).default,
  },
  "gate-tryouts-b-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-b-02-lod2.obj?url')).default,
  },
  "gate-tryouts-b-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-b-03-lod2.obj?url')).default,
  },
  "gate-tryouts-b-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-b-04-lod2.obj?url')).default,
  },
  "gate-tryouts-b-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-b-05-lod2.obj?url')).default,
  },
  "gate-tryouts-b-06": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-b-06-lod2.obj?url')).default,
  },
  "gate-tryouts-c-01-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-01-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-01-01-trigger.obj?url')).default,
  },
  "gate-tryouts-c-01-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-01-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-01-02-trigger.obj?url')).default,
  },
  "gate-tryouts-c-01-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-01-03-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-01-03-trigger.obj?url')).default,
  },
  "gate-tryouts-c-01-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-01-04-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-01-04-trigger.obj?url')).default,
  },
  "gate-tryouts-c-01-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-01-05-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-01-05-trigger.obj?url')).default,
  },
  "gate-tryouts-c-01-06": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-01-06-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-01-06-trigger.obj?url')).default,
  },
  "gate-tryouts-c-02-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-02-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-02-01-trigger.obj?url')).default,
  },
  "gate-tryouts-c-02-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-02-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-02-02-trigger.obj?url')).default,
  },
  "gate-tryouts-c-02-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-02-03-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-02-03-trigger.obj?url')).default,
  },
  "gate-tryouts-c-02-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-02-04-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-02-04-trigger.obj?url')).default,
  },
  "gate-tryouts-c-02-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-02-05-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-02-05-trigger.obj?url')).default,
  },
  "gate-tryouts-c-02-06": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-02-06-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-02-06-trigger.obj?url')).default,
  },
  "gate-tryouts-c-03-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-03-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-03-01-trigger.obj?url')).default,
  },
  "gate-tryouts-c-03-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-03-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-03-02-trigger.obj?url')).default,
  },
  "gate-tryouts-c-03-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-03-03-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-03-03-trigger.obj?url')).default,
  },
  "gate-tryouts-c-03-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-03-04-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-03-04-trigger.obj?url')).default,
  },
  "gate-tryouts-c-03-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-03-05-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-03-05-trigger.obj?url')).default,
  },
  "gate-tryouts-c-03-06": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-03-06-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-03-06-trigger.obj?url')).default,
  },
  "gate-tryouts-c-04-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-04-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-04-01-trigger.obj?url')).default,
  },
  "gate-tryouts-c-04-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-04-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-04-02-trigger.obj?url')).default,
  },
  "gate-tryouts-c-04-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-04-03-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-04-03-trigger.obj?url')).default,
  },
  "gate-tryouts-c-04-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-04-04-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-04-04-trigger.obj?url')).default,
  },
  "gate-tryouts-c-04-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-04-05-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-04-05-trigger.obj?url')).default,
  },
  "gate-tryouts-c-04-06": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-04-06-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-04-06-trigger.obj?url')).default,
  },
  "gate-tryouts-c-05-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-05-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-05-01-trigger.obj?url')).default,
  },
  "gate-tryouts-c-05-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-05-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-05-02-trigger.obj?url')).default,
  },
  "gate-tryouts-c-05-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-05-03-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-05-03-trigger.obj?url')).default,
  },
  "gate-tryouts-c-05-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-05-04-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-05-04-trigger.obj?url')).default,
  },
  "gate-tryouts-c-05-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-05-05-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-05-05-trigger.obj?url')).default,
  },
  "gate-tryouts-c-05-06": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-05-06-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-05-06-trigger.obj?url')).default,
  },
  "gate-tryouts-c-06-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-06-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-06-01-trigger.obj?url')).default,
  },
  "gate-tryouts-c-06-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-06-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-06-02-trigger.obj?url')).default,
  },
  "gate-tryouts-c-06-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-06-03-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-06-03-trigger.obj?url')).default,
  },
  "gate-tryouts-c-06-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-06-04-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-06-04-trigger.obj?url')).default,
  },
  "gate-tryouts-c-06-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-06-05-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-06-05-trigger.obj?url')).default,
  },
  "gate-tryouts-c-06-06": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-06-06-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-c-06-06-trigger.obj?url')).default,
  },
  "gate-tryouts-d-01": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-d-01-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-d-01-trigger.obj?url')).default,
  },
  "gate-tryouts-d-02": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-d-02-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-d-02-trigger.obj?url')).default,
  },
  "gate-tryouts-d-03": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-d-03-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-d-03-trigger.obj?url')).default,
  },
  "gate-tryouts-d-04": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-d-04-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-d-04-trigger.obj?url')).default,
  },
  "gate-tryouts-d-05": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-d-05-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-d-05-trigger.obj?url')).default,
  },
  "gate-tryouts-d-06": {
    "lod": async () => (await import('assets/replay_assets/gates/gate-tryouts-d-06-lod2.obj?url')).default,
    "trigger": async () => (await import('assets/replay_assets/gates/gate-tryouts-d-06-trigger.obj?url')).default,
  },
}

const moduleMapping = {
  // Gate A (Gate)
  // X = Y = Variable
  // Z = FIX
  "gate-a": (name, x, y, z) => `${name}-0${x}`,
  "gate-tryouts-a": (name, x, y, z) => `${name}-0${x}`,
  // Gate B (Pole)
  // X, Z = FIX
  // Y = Variable
  "gate-b": (name, x, y, z) => `${name}-0${y}`,
  "gate-tryouts-b": (name, x, y, z) => `${name}-0${y}`,
  // Gate C (Gate With poles )
  // X = Z = Variable
  // Y = Variable
  "gate-c": (name, x, y, z) => `${name}-0${x}-0${y}`,
  "gate-tryouts-c": (name, x, y, z) => `${name}-0${x}-0${y}`,
  // Gate D (Cube)
  // X = Y = Z = Variable
  "gate-d": (name, x, y, z) => `${name}-0${x}`,
  "gate-tryouts-d": (name, x, y, z) => `${name}-0${x}`,
}
const getNameByModuleScale = function (name, x, y, z) {
  let moduleMappingElement = moduleMapping[name]
  if(!moduleMappingElement) {
    console.error("no module mapping for gate: ", name)
    return
  }
  return moduleMapping[name](name, Math.round(x), Math.round(y), Math.round(z))
}

const loader = new OBJLoader()
const assetCache = {}

const assetObjLoader = async function (url) {
  if(assetCache[url]){
    return assetCache[url].clone()
  }
  return new Promise((resolve, reject) => {
    // console.log("loading resource: " + url)
    // console.log(url)
    try {
      loader.load(
        url,
        function (object) {
          assetCache[url] = object
          resolve(object.clone())
        },
        function (xhr) {
          console.log(`${url}${(xhr.loaded / xhr.total * 100)}% loaded`);
        },
        function (error) {
          console.log('An error happened during loading of the asset:' + url, error);
          reject(error)
        }
      )
    } catch (e) {
      console.error("error loading asset: " +url, e);
    }
  })
}

export { assetLoader, assetObjLoader, getNameByModuleScale }
