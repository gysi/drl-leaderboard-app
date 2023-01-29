<template>
  <div class="slotmachine container">
    <div class="result">
      <div class="lights">
        <span v-for="n in lightsTop" :ref="(el) => lightsTopSpans.push(el)" :key="n"></span>
      </div>
      <div ref="resultScreen" class="result-screen">Let's play!</div>
    </div>
    <div class="game-board">
      <div class="lights">
        <span v-for="n in lightsBottom" :ref="(el) => lightsBottomSpans.push(el)" :key="n"></span>
      </div>
      <div class="cards-container">
        <div ref="gate" class="gate">
          <q-icon ref="tip1" name="arrow_right" class="fas fa-tip-1" />
          <button ref="reset" class="reset" v-on:click="resetGame">
            LOAD TRACKS
          </button>
        </div>
        <div ref="cardsDiv" class="card" id="card-1">
          <ul ref="cardUl">
          </ul>
        </div>
      </div>
      <div class="arm">
        <q-icon ref="tip2" name="arrow_drop_up" class="fas fa-tip-2" />
        <span class="arm-span1"><span ref="armGrabStart"></span></span>
        <span ref="armSpan" class="arm-span">
            <span
              ref="armGrab"
              v-on:mousedown="myTriggerTopMouseDown"
              v-on:mouseup="myTriggerTopMouseUp"> </span>
          </span>
        <span class="arm-span2"><span ref="armGrabEnd"></span></span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SlotMachine',
  props: {
    cards: {
      type: Array,
      required: true
    },
  },
  watch: {
    cards() {
      console.log("cards changed", this.isMachineWorks);
      if(this.isMachineWorks === true){
        console.log("machine is working set to true");
        this.cardsHaveChanged = true;
      }else if(this.isMachineWorks === false && this.isGateClosed === false){
        this.rollGateUp();
      }
    },
  },
  setup() {
    return {
      timeGameOverScreen: 150,
      timeRoll: 2000,
      timeUntilAnimationStarts: 6000,
      timeFinishAnim: 7000,
    }
  },
  mounted() {
    document.body.addEventListener("mouseup", this.bodyOnMouseUp);
    document.body.addEventListener("mousemove", this.bodyOnMouseMove);
  },
  beforeUnmount() {
    document.body.removeEventListener("mouseup", this.bodyOnMouseUp);
    document.body.removeEventListener("mousemove", this.bodyOnMouseMove);
  },
  data() {
    return {
      lightsTop: 10,
      lightsTopSpans: [],
      lightsBottom: 13,
      lightsBottomSpans: [],
      isGameOver: true,
      isMachineWorks: false,
      isTriggerOn: false,
      firstGame: true,
      loadedCards: [],
      cardsHaveChanged: false,
      isGateClosed: true,
    }
  },
  methods: {
    handleGameStart() {
      this.isMachineWorks = true;
      this.$refs.armSpan.classList.add("arm-clicked");
      setTimeout(() => {
        this.$refs.armSpan.classList.remove("arm-clicked");
      }, this.timeRoll);
      this.drawRandomItems();
    },
    rollGateUp() {
      this.$refs.gate.style.transform = "translateY(0%)";
      this.isGateClosed = true;
    },
    resetGame() {
      if(this.cards.length === 0){
        this.screenAlert("No tracks to load!");
        return;
      }
      this.loadedCards = this.cards;

      const cardUl = this.$refs.cardUl;
      cardUl.style.transition = "none";
      cardUl.innerHTML = "";
      for (let i = 0; i < 3; i++) {
        const li = document.createElement("li");
        li.className = "slotMachineCardItem";
        li.textContent = this.loadedCards[i];
        li.dataset.index = i;
        cardUl.append(li);
      }
      const cardsDivHeight = Number(getComputedStyle(this.$refs.cardsDiv).height.slice(0,-2));
      const liHeight = Number(getComputedStyle(this.$refs.cardUl.children[0]).height.slice(0,-2));
      cardUl.style.bottom = ((cardsDivHeight * -0.25) + liHeight) + "px";
      setTimeout(() => {
        cardUl.style.transition = "bottom 6s cubic-bezier(0.5, 0, 0, 1) 0s";
      }, 100);

      this.handleTips(1);
      this.screenAlert("Grab the arm!");
      this.$refs.gate.style.transform = "translateY(100%)";
      this.isGateClosed = false;

      setTimeout(() => {
        this.isMachineWorks = false;
      }, this.timeGameOverScreen);
    },
    myTriggerTopMouseDown(e) {
      if (this.isMachineWorks || this.isGateClosed) {
        return;
      } else {
        this.isTriggerOn = true;
      }
    },
    myTriggerTopMouseUp(){
      if (this.isTriggerOn) {
        this.isTriggerOn = false;
        this.$refs.armSpan.style.transition = "transform 1s";
        this.$refs.armSpan.style.transform = "rotate(15deg)";
        setTimeout(() => {
          this.$refs.armSpan.style.transition = "none";
        }, 1000);
      }
    },
    bodyOnMouseUp() {
      if (this.isTriggerOn) {
        this.isTriggerOn = false;
        this.$refs.armSpan.style.transition = "transform 1s";
        this.$refs.armSpan.style.transform = "rotate(15deg)";
        setTimeout(() => {
          this.$refs.armSpan.style.transition = "none";
        }, 1000);
      }
    },
    bodyOnMouseMove(e) {
      if (this.isTriggerOn) {
        const armGrabStartTop = this.$refs.armGrabStart.getBoundingClientRect().y;
        const armGrabEndTop = this.$refs.armGrabEnd.getBoundingClientRect().y;
        const angleArm = 165;
        const betweenArmSpans = armGrabEndTop - armGrabStartTop;
        const cursorPositionYMinusGrabStartTop = e.clientY - armGrabStartTop;
        const newDeg = (cursorPositionYMinusGrabStartTop * angleArm) / betweenArmSpans;
        if(newDeg <= 15) {
          return;
        }
        this.$refs.armSpan.style.transform = `rotate(${newDeg}deg)`;
        if (newDeg >= 165) {
          this.isTriggerOn = false;
          this.handleTips(2);
          this.screenAlert("Lets ROLLLL!!!")
          this.handleGameStart();
          this.$refs.armSpan.style.transition = "transform 1s";
          this.$refs.armSpan.style.transform = "rotate(15deg)";
          setTimeout(() => {
            this.$refs.armSpan.style.transition = "none";
          }, 1000);
        }
      }
    },
    handleTips(tip) {
      if (this.firstGame) {
        switch (tip) {
          case 1:
            this.$refs.tip1.$el.style.display = "none";
            setTimeout(() => (this.$refs.tip2.$el.style.display = "inline"), 200);
            break;
          case 2:
            this.$refs.tip2.$el.style.display = "none";
            this.firstGame = false;
            break;
          default:
            break;
        }
      }
    },
    screenAlert(input) {
      this.$refs.resultScreen.textContent = input;
      this.$refs.resultScreen.classList.add("alert");
      setTimeout(() => {
        this.$refs.resultScreen.classList.remove("alert");
      }, 1000);
    },
    liCreator() {
      const cardUl = this.$refs.cardUl;
      const liElements = [...cardUl.children].slice(-3);
      cardUl.style.transition = "none";
      cardUl.innerHTML = "";
      cardUl.append(...liElements);
      const cardsDivHeight = Number(getComputedStyle(this.$refs.cardsDiv).height.slice(0,-2));
      const liHeight = Number(getComputedStyle(liElements[0]).height.slice(0,-2));
      cardUl.style.bottom = ((cardsDivHeight * -0.25) + liHeight) + "px";
      setTimeout(() => {
        cardUl.style.transition = "bottom 6s cubic-bezier(0.5, 0, 0, 1) 0s";
      }, 100);
    },
    animationFinish() {
      // this.$refs.coinsReward.parentNode.style.filter = "brightness(135%)";
      // this.$refs.coinsReward.classList.add("active-anim");
      // this.$refs.coinsFall.classList.add("active-anim");
      let topTime = 35;
      this.lightsTopSpans.forEach(item => {
        item.classList.remove("light-active");
        setTimeout(() => {
          item.classList.add("light-active");
        }, topTime);
        topTime += 35;
      });
      this.lightsBottomSpans.forEach(item => {
        item.classList.remove("light-active");
        setTimeout(() => {
          item.classList.add("light-active");
        }, topTime);
        topTime += 35;
      });
    },
    drawRandomItems() {
      const cardUl = this.$refs.cardUl;
      const randomNumberDraw = Math.floor(Math.random() * 9 + 80);
      let indexDraw = Math.floor(Math.random() * this.loadedCards.length);
      let liArray = [];
      for (let i = 0; i < randomNumberDraw; i++) {
        if (indexDraw === this.loadedCards.length) indexDraw = 0;
        const li = document.createElement("li");
        li.className = "slotMachineCardItem";
        li.textContent = this.loadedCards[indexDraw];
        li.dataset.index = indexDraw;
        liArray.push(li);
        indexDraw++;
      }
      cardUl.append(...liArray);
      const cardsDivHeight = Number(getComputedStyle(this.$refs.cardsDiv).height.slice(0,-2));
      const height = Number(getComputedStyle(cardUl.children[0]).height.slice(0,-2));
      cardUl.style.bottom = (height * (cardUl.children.length-2))-(cardsDivHeight*0.25) + "px";
      setTimeout(this.animationFinish, this.timeUntilAnimationStarts);
      setTimeout(() => {
        this.liCreator();
        this.isMachineWorks = false;
        this.screenAlert("Here is your track!");
        if(this.cardsHaveChanged){
          this.rollGateUp();
          this.cardsHaveChanged = false;
        }
      }, this.timeFinishAnim);
    }
  }
}
</script>

<style lang="scss" scoped>
@keyframes armHand {
  0% {
    transform: rotate(15deg);
  }

  50% {
    transform: rotate(165deg);
  }

  100% {
    transform: rotate(15deg);
  }
}
@keyframes alert {
  0% {
    color: rgba(var(--keyframes-alert), 1);
  }

  50% {
    color: rgba(var(--keyframes-alert), 0.35);
  }

  100% {
    color: rgba(var(--keyframes-alert), 1);
  }
}
@keyframes lightsOn {
  0% {
    box-shadow: inset 0 0 5px 0 rgba(0, 0, 0, 0.5), 0 0 0px 0 var(--keyframes-lightsOn-shadow);
    background: var(--keyframes-lightsOff);
  }
  50% {
    box-shadow: inset 0 0 5px 0 rgba(0, 0, 0, 0.5), 0 0 10px 2px var(--keyframes-lightsOff-shadow);
    background: var(--keyframes-lightsOn);
  }
  100% {
    box-shadow: inset 0 0 5px 0 rgba(0, 0, 0, 0.5), 0 0 0px 0 var(--keyframes-lightsOn-shadow);
    background: var(--keyframes-lightsOff);
  }
}

.slotmachine *{
  user-select: none;
  box-sizing: border-box;
  padding: 0;
  margin: 0;
}
.container {
  //max-width: 800px;
  display: flex;
  flex-direction: column;
  position: relative;
}

.lights {
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: space-evenly;

  span {
    margin-top: 10px;
    box-shadow: inset 0 0 5px 0 rgba(0, 0, 0, 0.5), 0 0 0px 0 var(--keyframes-lightsOff-shadow);;
    width: 1.75vmin;
    height: 1.75vmin;
    border-radius: 50%;
    background: var(--keyframes-lightsOff);
    &.light-active {
      animation: lightsOn 0.25s linear;
      animation-iteration-count: 10;
    }
  }
}

.result {
  position: relative;
  width: 40vmin;
  height: 13vmin;
  background: var(--result-background);
  margin: 0 auto;
  border-radius: 30px 30px 0 0;
  box-shadow: 3px -3px 3px 3px rgba(0, 0, 0, 0.5),
  inset 5px 0px 0px 0px rgba(255, 255, 255, 0.3);

  .result-screen {
    box-shadow: inset 3px 3px 3px 3px rgba(0, 0, 0, 0.3);
    letter-spacing: -1px;
    border-radius: 20px;
    position: absolute;
    left: 50%;
    top: 60%;
    transform: translate(-50%, -50%);
    width: 85%;
    height: 50%;
    text-align: center;
    font-size: 2.25vmin;
    padding-top: 1.8vmin;
    //text-transform: uppercase;
    background: var(--result-screen-background);
    color: var(--result-screen-text-color);
    overflow: hidden;
    cursor: pointer;

    &:hover::before {
      box-shadow: 0 -10px 25px 15px rgba(255, 255, 255, 0.25);
    }
    &.alert {
      animation: alert 0.35s linear 0s infinite;
    }

    &:before {
      content: '';
      position: absolute;
      left: 50%;
      top: 50%;
      transition: box-shadow 0.15s;
      width: 300px;
      transform: translate(-50%, -50%) rotate(15deg);
      background: rgba(255, 255, 255, 0.35);
      box-shadow: 0 5px 25px 15px rgba(255, 255, 255, 0.25);
    }
  }
}

.game-board {
  position: relative;
  width: 65vmin;
  height: 35vmin;
  margin: 0 auto;
  background: var(--gameboard-background);
  border-radius: 2.5vmin 2.5vmin 2.5vmin 2.5vmin;
  box-shadow: 3px -3px 3px 3px rgba(0, 0, 0, 0.5),
  inset 5px 0px 0px 0px rgba(255, 255, 255, 0.3);

  .cards-container {
    overflow: hidden;
    width: 90%;
    height: 80%;
    position: absolute;
    left: 50%;
    top: 52.5%;
    transform: translate(-50%, -50%);
    background: var(--cards-container-brackground);
    border-radius: 25px;
    display: flex;
    justify-content: space-around;
    align-items: center;
    box-shadow: inset rgba(26, 26, 26, 0.6) 3px 4px 6px 5px,
    inset var(--cards-container-inset) 0 -1px 3px 10px, inset rgb(0, 0, 0) 0 2px 5px 5px,
    rgba(41, 31, 6, 0.4) -2px -2px 10px 3px;

    .gate {
      display: flex;
      position: absolute;
      cursor: default;
      height: 100%;
      width: 100%;
      z-index: 1;
      transform: translateY(0);
      transition: transform 1.5s;
      border-radius: 25px;
      box-shadow: inset hsla(0, 0%, 15%, 0.8) 0 -1px 5px 4px,
      inset hsla(39, 100%, 12%, 0.25) 0 -1px 2px 7px,
      inset hsla(0, 0%, 70%, 0.7) 0 2px 3px 7px;
      background-image: -webkit-repeating-linear-gradient(
          left,
          hsla(0, 0%, 70%, 0) 0%,
          hsla(0, 0%, 70%, 0) 6%,
          hsla(0, 0%, 70%, 0.1) 7.5%
      ),
      -webkit-repeating-linear-gradient(left, hsla(0, 0%, 0%, 0) 0%, hsla(
            0,
            0%,
            0%,
            0
        )
        4%, hsla(0, 0%, 0%, 0.03) 4.5%),
      -webkit-repeating-linear-gradient(left, hsla(0, 0%, 100%, 0) 0%, hsla(
            0,
            0%,
            70%,
            0
        )
        1.2%, hsla(0, 0%, 100%, 0.15) 2.2%),
      linear-gradient(
          180deg,
          hsl(0, 0%, 60%) 0%,
          hsl(0, 0%, 70%) 47%,
          hsl(0, 0%, 80%) 53%,
          hsl(0, 0%, 40%) 100%
      );
      justify-content: center;
      align-items: center;
      align-content: center;
      flex-wrap: wrap;
      text-align: center;

      .gate-text,
      .gate-subtext {
        width: 100%;
        margin: 10px;
        text-shadow: hsla(0, 0%, 40%, 0.5) 0 -1px 0,
        hsla(0, 0%, 100%, 0.6) 0 2px 1px;
        color: rgb(90, 90, 90);
      }

      .gate-text {
        font-size: 4vmin;
      }

      .gate-subtext {
        line-height: 2vmin;
        font-size: 2vmin;
        color: grey;
      }

      .reset {
        line-height: 2vmin;
        font-size: 2vmin;
        padding-top: .5vmin;
        font-weight: 500;
        cursor: pointer;
        transition: transform 0.15s, box-shadow 0.15s;
        display: block;
        width: 32vmin;
        height: 6vmin;
        border: none;
        border-radius: 20px;
        box-shadow: inset #7c7c7ccc 0 -1px 5px 4px,
        inset #ffffffb3 0 -1px 0px 7px, inset #27272740 0 2px 1px 7px,
        2px 3px 3px 0 rgb(85, 85, 85);
        background-image: -webkit-repeating-linear-gradient(
            left,
            hsla(0, 0%, 100%, 0) 0%,
            hsla(0, 0%, 100%, 0) 6%,
            hsla(0, 0%, 100%, 0.1) 7.5%
        ),
        -webkit-repeating-linear-gradient(left, hsla(0, 0%, 0%, 0) 0%, hsla(
              0,
              0%,
              0%,
              0
          )
          4%, hsla(0, 0%, 0%, 0.03) 4.5%),
        -webkit-repeating-linear-gradient(left, hsla(0, 0%, 100%, 0) 0%, hsla(
              0,
              0%,
              100%,
              0
          )
          1.2%, hsla(0, 0%, 100%, 0.15) 2.2%),
        linear-gradient(
            180deg,
            hsl(0, 0%, 78%) 0%,
            hsl(0, 0%, 90%) 47%,
            hsl(0, 0%, 78%) 53%,
            hsl(0, 0%, 70%) 100%
        );

        &:hover {
          transform: translate(3px, 3px);
          box-shadow: inset #7c7c7ccc 0 -1px 5px 4px,
          inset #ffffffb3 0 -1px 0px 7px, inset #27272740 0 2px 1px 7px,
          0 0 0 0 rgb(85, 85, 85);
        }
      }
    }

    &:before {
      content: '';
      position: absolute;
      width: 700px;
      transform: rotate(-10deg);
      background: rgba(255, 255, 255, 0.5);
      box-shadow: 0 0 50px 50px rgba(255, 255, 255, 0.3);
      z-index: 1;
      filter: brightness(100%);
      transition: box-shadow 0.3s;
    }

    &:after {
      content: '';
      position: absolute;
      width: 100%;
      height: 100%;
      background: rgba(155, 155, 155, 0.1);
      transition: background 0.3s;
      box-shadow: inset 15px 15px 50px 0px rgba(214, 214, 214, 0.25),
      inset -10px -10px 30px 0 rgba(180, 180, 180, 0.5);
    }

    &:hover {
      cursor: pointer;

      &::before {
        box-shadow: 0 -35px 50px 50px rgba(255, 255, 255, 0.3);
      }

      &::after {
        background: rgba(255, 255, 255, 0.15);
      }
    }

    .card {
      width: 85%;
      height: 20vmin;
      background: silver;
      border: 3px solid #603912;
      border-radius: 5px;
      box-shadow: 0 0 0 10px var(--card-border-color),
        inset 0 0 20px 0 rgba(0, 0, 0, 0.75);
      position: relative;
      overflow: hidden;
      font-size: 2.8vmin;
      color: black;

      ul {
        list-style-type: none;
        position: absolute;
        width: 100%;
        height: 100%;
        left: 0;
        bottom: -25%;
        transition: bottom 6s cubic-bezier(0.5, 0, 0, 1) 0s;
      }
    }
  }

  .arm {
    position: absolute;
    width: 4.8vmin;
    height: 12vmin;
    background: linear-gradient(
        var(--arm-base-gradient-top),
        var(--arm-base-gradient-middle),
        var(--arm-base-gradient-bottom)
    );
    left: 100%;
    top: 50%;
    transform: translateY(-50%);
    border-radius: 0 100% 100% 0;
    cursor: pointer;
    box-shadow: inset 0px 0px 10px 0px rgba(0, 0, 0, 0.5),
    rgba(0, 0, 0, 0.75) 12px 10px 10px 2px;

    &:hover .arm-span span {
      box-shadow: inset var(--grab-shadow-hover-color) 0 -1px 7px 4px,
      inset var(--grab-shadow-inset-color) 0 2px 1px -10px,
    }

    &:after {
      content: '';
      position: absolute;
      width: 4vmin;
      height: 12vmin;
      background: linear-gradient(
          var(--arm-base-gradient-top),
          var(--arm-base-gradient-middle),
          var(--arm-base-gradient-bottom)
      );
      left: 0;
      top: 50%;
      transform: translateY(-50%);
      border-radius: 0 100% 100% 0;
      cursor: pointer;
      box-shadow: inset 0px 0px 10px 0px rgba(0, 0, 0, 0.5),
      rgba(0, 0, 0, 0.4) 7px 0 7px 0px;
    }

    .arm-span, .arm-span1, .arm-span2{
      position: absolute;
      width: 2.25vmin;
      height: 18vmin;
      background: linear-gradient(#ab9e4f, rgb(176, 126, 82));
      left: 10%;
      bottom: 50%;
      transform-origin: bottom;
      border-radius: 100% 100% 75% 75%;
      box-shadow: inset 0px 0px 10px 0px rgba(0, 0, 0, 0.9),
      rgba(0, 0, 0, 0.4) 10px 5px 7px 2px;

       //&.arm-clicked {
       //  animation: armHand 1.5s;
       //}

      span {
        position: absolute;
        width: 5vmin;
        height: 5vmin;
        border-radius: 50%;
        right: -3.75vmin;
        top: -3.25vmin;
        transform: translateX(-50%);
        background: var(--grab-background-color);
        box-shadow: inset var(--grab-shadow-color) 0 -1px 7px 4px,
        inset var(--grab-inset-color) 0 2px 1px -10px
      }

    }
    .arm-span {
      transform: rotate(15deg);
    }
    .arm-span1 {
      transform: rotate(15deg);
      visibility: hidden;
    }
    .arm-span2 {
      transform: rotate(165deg);
      visibility: hidden;
    }
  }
}

.fas {
  position: absolute;
  //font-size: 1000vmin;
  font-size: 7vmin;
  color: rgba(255, 255, 255, 0);
  text-shadow: 0 0 5px rgba(255, 215, 0, 0);
  z-index: 1;
  &.fa-tip-1 {
    left: 10%;
    bottom: 24%;
    transform: translate(0, -50%);
    animation: tipOpacityAnimation .75s ease infinite, tip1Anim .75s ease infinite;
  }
  &.fa-tip-2 {
    display: none;
    left: 80%;
    bottom: 70%;
    transform: translate(0, -50%);
    animation: tipOpacityAnimation .75s ease infinite, tip2Anim .75s ease infinite;
  }
}

@keyframes tipOpacityAnimation {
  0% {
    text-shadow: 0 0 5px rgba(255, 215, 0, 0);
    color: rgba(255, 215, 0, 0);
  }
  20% {
    text-shadow: 0 0 5px rgba(255, 215, 0, 1);
    color: rgba(255, 223, 51, 1);
  }
  100% {
    text-shadow: 0 0 5px rgba(255, 215, 0, 1);
    color: rgba(255, 255, 255, 1);
  }
}
@keyframes tip1Anim {
  0% {
    transform: translate(0, -50%);
  }
  100% {
    transform: translate(175%, -50%);
  }
}
@keyframes tip2Anim {
  0% {
    transform: translate(0, -50%);
  }
  100% {
    transform: translate(0, -120%);
  }
}
</style>

<style lang="scss">
.slotMachineCardItem {
  height: 50%;
  line-height: 9vmin;
  text-align: center;

  &::after {
    content: '';
    position: relative;
    display: block;
    width: 100%;
    height: 1px;
    left: 0;
    top: 0;
    bottom: 0;
    background: linear-gradient(to right,
      //rgba(0, 0, 0, 0.6) 0%,
      //rgba(255, 255, 255, 0) 30%,
      //rgba(255, 255, 255, 0) 70%,
      //rgba(0, 0, 0, 0.6) 100%
      rgba(255, 255, 255, 0) 0%,
      rgba(0, 0, 0, 0.6) 5%,
      rgba(0, 0, 0, 0.6) 95%,
      rgba(255, 255, 255, 0) 100%
    );
  }
}
</style>
