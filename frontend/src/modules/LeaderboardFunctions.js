import {format, formatDuration, intervalToDuration, parseISO} from "date-fns";
import {utcToZonedTime, zonedTimeToUtc} from 'date-fns-tz';

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

function formatMilliSeconds(milliseconds, addPlusSign = false) {
  let isNegative = false;
  if(milliseconds < 0 ) {
    isNegative = true;
    milliseconds = milliseconds * -1;
  }
  const date = new Date(milliseconds);
  let hours = (date.getUTCHours()).toString().padStart(2, '0');
  let minutes = date.getUTCMinutes().toString().padStart(2, '0');
  let seconds = date.getUTCSeconds().toString().padStart(2, '0');
  if(hours <= 0 && minutes <= 0 && seconds < 10){
    seconds = seconds.substring(1);
  }
  let millis = date.getUTCMilliseconds().toString().padStart(3, '0');
  return `${isNegative ? '- ' : addPlusSign ? '+ ' : ''}${hours > 0 ? hours + ':' : ''}${minutes > 0 || hours > 0 ? minutes + ':' : ''}${seconds > 0 || minutes > 0 || hours > 0 ? seconds : '0'}.${millis}`;
}

function substractAndformatMilliSeconds(millisecondsHigher, millisecondsLower){
  return formatMilliSeconds(millisecondsHigher - millisecondsLower, true);
}

function getDateDifference(dateString) {
  if(!dateString) return '';
  let duration = intervalToDuration({
    start: new Date(dateString+'Z'),
    end: new Date()
  });
  let units = [];
  if (duration.years > 0) {
    units.push('years');
  } else if(duration.months > 0 ){
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

function getDateDifferenceToNowByGermanTimezone(dateString) {
  if(!dateString) return '';
  let germanToUtcDate = zonedTimeToUtc(dateString, 'Europe/Berlin');
  let duration = intervalToDuration({
    start: germanToUtcDate,
    end: new Date()
  });
  let units = [];
  if(duration.years > 0) {
    units.push('years')
  } else if(duration.months > 0 ){
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

function formatISODateTimeToDate (isoDateTime) {
  if (isoDateTime == null) return;
  const date = parseISO(isoDateTime)
  return format(date, 'MMM do, yyyy');
}

const toLocalDateformat = (val) => {
  if (val) {
    const date = new Date(val+'Z')
    const userTimezone = Intl.DateTimeFormat().resolvedOptions().timeZone
    const zonedDate = utcToZonedTime(date, userTimezone);
    const pattern = 'yyyy-MM-dd HH:mm:ss'
    return format(zonedDate, pattern, { timeZone: userTimezone }) + ' ' + userTimezone
  }
};

function sortByBeatenBy(a, b, rowa, rowb) {
  // shouldn't happen, but just to make sure its not null
  if (a == null && b == null) {
    return 0;
  } else if (a == null) {
    return -1;
  } else if (!b == null) {
    return 1;
  }
  if (a.length === 0 && b.length === 0) {
    const aDate = new Date(rowa.createdAt + 'Z');
    const bDate = new Date(rowb.createdAt + 'Z');
    if (aDate < bDate) {
      return -1;
    } else {
      return 1;
    }
  } else if (a.length === 0) {
    return -1;
  } else if (b.length === 0) {
    return 1;
  }

  if (a.length === b.length) {
    const aDate = new Date(a[0].createdAt + 'Z');
    const bDate = new Date(b[0].createdAt + 'Z');
    if (aDate < bDate) {
      return 1;
    } else {
      return -1;
    }
  } else if (a.length < b.length) {
    return -1;
  } else {
    return 1;
  }
}

export { backGroundColorByPosition, formatMilliSeconds, getDateDifference, substractAndformatMilliSeconds,
  getDateDifferenceToNowByGermanTimezone, formatISODateTimeToDate, toLocalDateformat, sortByBeatenBy }
