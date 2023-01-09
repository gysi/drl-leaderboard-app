<template>
  <q-page class="q-pa-md">
    <q-card bordered class="bg-grey-9 text-white" style="max-width: 50%">
      <q-card-section>
        <div class="text-h6">How does the point system work?</div>
      </q-card-section>

      <q-separator dark inset />

      <q-card-section class="bg-white text-black" style="font-size: 16px">
        <div>The number of points a player receives is calculated based on their position on the leaderboard, with the formula</div>
        <b><span>points = 101 - position</span></b>
        <div>Additionally the player is awarded bonus points below certain positions</div>
        <div>
          <q-markup-table class="q-ma-sm">
            <thead>
            <tr>
              <th class="text-center">Position</th>
              <th class="text-center">Bonus Points</th>
            </tr>
            </thead>
            <tbody class="text-center">
            <tr>
              <td>&lt;= 75</td>
              <td>+5</td>
            </tr>
            <tr>
              <td>&lt;= 50</td>
              <td>+5</td>
            </tr>
            <tr>
              <td>&lt;= 25</td>
              <td>+5</td>
            </tr>
            <tr>
              <td>&lt;= 10</td>
              <td>+5</td>
            </tr>
            <tr>
              <td>&lt;= 5</td>
              <td>+5</td>
            </tr>
            <tr>
              <td>&lt;= 3</td>
              <td>+2</td>
            </tr>
            <tr>
              <td>= 1</td>
              <td>+2</td>
            </tr>
            </tbody>
          </q-markup-table>
        </div>
        <span>The final number of points a player earns is then raised to the power of <b>1.1</b> .</span>
        <span>Here are some expamles:</span>
        <div>
          <q-markup-table class="q-ma-sm">
            <thead>
            <tr>
              <th class="text-center">Position</th>
              <th class="text-center">Points earned</th>
            </tr>
            </thead>
            <tbody class="text-center">
            <tr>
              <td>1</td>
              <td>(100 + 26)<sup>1.1</sup> = <b>204.365...</b></td>
            </tr>
            <tr>
              <td>2</td>
              <td>(99 + 24)<sup>1.1</sup> = <b>199.019...</b></td>
            </tr>
            <tr>
              <td>3</td>
              <td>(98 + 24)<sup>1.1</sup> = <b>197.240...</b></td>
            </tr>
            <tr>
              <td>4</td>
              <td>(97 + 22)<sup>1.1</sup> = <b>191.911...</b></td>
            </tr>
            <tr>
              <td>5</td>
              <td>(96 + 22)<sup>1.1</sup> = <b>190.138...</b></td>
            </tr>
            <tr>
              <td>10</td>
              <td>(91 + 20)<sup>1.1</sup> = <b>177.768...</b></td>
            </tr>
            <tr>
              <td>25</td>
              <td>(76 + 15)<sup>1.1</sup> = <b>142.871...</b></td>
            </tr>
            <tr>
              <td>50</td>
              <td>(51 + 10)<sup>1.1</sup> = <b>92.015...</b></td>
            </tr>
            <tr>
              <td>75</td>
              <td>(26 + 5)<sup>1.1</sup> = <b>43.701...</b></td>
            </tr>
            <tr>
              <td>100</td>
              <td>1<sup>1.1</sup> = <b>1</b></td>
            </tr>
            </tbody>
          </q-markup-table>
        </div>
        <span>For the display, the points are rounded to a whole number. However, the total points in the overall ranking is accurate to 14 digits and is only then rounded.</span>
      </q-card-section>
    </q-card>
  </q-page>
</template>

<script>
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'FAQPage'
})
</script>

<style lang="sass" scoped>
.q-table thead th,
.q-table tbody td
  font-size: 16px
</style>
