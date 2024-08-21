import bronze from "assets/awards/24_summer_contribution_bronze_small.png"
import silver from "assets/awards/24_summer_contribution_silver_small.png"
import gold from "assets/awards/24_summer_contribution_gold_small.png"
import diamond from "assets/awards/24_summer_contribution_diamond_small.png"

const awards = {
  // > 10
  "2024-season-bronze": {
    "asset": bronze,
    "tooltip": '24 Summer Season Bronze Contributor'
  },
  // > 50
  "2024-season-silver": {
    "asset": silver,
    "tooltip": '24 Summer Season Silver Contributor'
  },
  // > 100
  "2024-season-gold": {
    "asset": gold,
    "tooltip": '24 Summer Season Gold Contributor'
  },
  // > 250
  "2024-season-diamond": {
    "asset": diamond,
    "tooltip": '24 Summer Season Diamond Contributor'
  }
}
const playerIdToAwardMap = {
  //gysi 5xx
  "547247576684373546": awards['2024-season-diamond'],
  //vgan 250
  "547247574603995155": awards['2024-season-diamond'],
  //ItsleeFpv 200
  "547247575287661837": awards['2024-season-gold'],
  // The Bob 200
  "547247575891648956": awards['2024-season-gold'],
  // tripp 100
  "547247575321225883": awards['2024-season-gold'],
  // Kung Fu Chicken (Gnostic)
  "547247577162521182": awards['2024-season-gold'],
  // streetz 20
  "547247575895839312": awards['2024-season-bronze'],
  // thesauceror
  "547247577200271842": awards['2024-season-bronze']
}

export { playerIdToAwardMap }
