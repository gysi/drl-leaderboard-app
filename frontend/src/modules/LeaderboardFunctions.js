import {formatDuration, intervalToDuration} from "date-fns";

function backGroundColorByPosition(position){
  if (position > 75) {
    return '#4B4B4B'
  }
  if (position > 50) {
    return '#234918'
  }
  if (position > 25) {
    return '#326722'
  }
  if (position > 10) {
    return '#40832d'
  }
  if (position > 3) {
    return '#59b43d'
  }
  if (position > 2) {
    return 'rgb(187,107,33)' //rgb(221,127,39) 3
  }
  if (position > 1) {
    return 'rgb(138,135,141)' //rgb(160,158,165) 2
  }
  return 'rgba(180,135,22)'; //rgb(236,160,28)
}

function formatMilliSeconds(milliseconds){
  const date = new Date(milliseconds);
  let hours = (date.getHours()-1).toString().padStart(2, '0');
  let minutes = date.getMinutes().toString().padStart(2, '0');
  let seconds = date.getSeconds().toString().padStart(2, '0');
  let millis = date.getMilliseconds().toString().padStart(3, '0');
  return `${hours > 0 ? hours + ':' : ''}${minutes}:${seconds}.${millis}`;
}

function getDateDifference(dateString) {
  // 2022-08-30T02:14:25.042
  let duration = intervalToDuration({
    start: new Date(dateString+'Z'),
    end: new Date()
  });
  let units = [];
  if(duration.months > 0 ){
    units.push('months');
  } else if(duration.days > 0 ){
    units.push('days');
  } else if(duration.hours > 0 ){
    units.push('hours');
  } else if(duration.minutes > 0 ){
    units.push('minutes');
  } else {
    units.push('seconds');
  }
  return formatDuration(duration, { format: units }) + ' ago';
}

export { backGroundColorByPosition, formatMilliSeconds, getDateDifference }
