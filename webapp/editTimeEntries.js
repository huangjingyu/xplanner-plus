var lastActualHours = 0.0;
var lastRemainingHours = 0.0;

function entryChanged(){
    var actualHours, difference;
    actualHours = computeActualHours();
    if (actualHours != lastActualHours){
        difference = lastActualHours - actualHours;
        remainingHours = lastRemainingHours + difference;
        saveRemainingHours(remainingHours);
        if (remainingHours < 0.0){
            remainingHours = 0.0;
            document.getElementById("remainingHoursLabel").style.color = "red";
        }
        else{
            document.getElementById("remainingHoursLabel").style.color = "black";
        } 
        remainingHours = Math.round(remainingHours * 10) / 10;
        document.forms["timelog"].remainingHours.value = remainingHours;
        saveActualHours(actualHours);
    }

}

function computeActualHours(){
    var duration, startDate, endDate, interval, rowCount, deleted, actualHours;
    rowCount = parseInt(document.forms["timelog"].elements["rowcount"].value);
    actualHours = 0.0;
    for (i = 0; i < rowCount; i++){
        duration = document.forms["timelog"].elements["duration["+ i +"]"] ? parseFloat(document.forms["timelog"].elements["duration["+ i +"]"].value) : 0.0;
        startDate = document.forms["timelog"].elements["startTime["+ i +"]"] ? document.forms["timelog"].elements["startTime["+ i +"]"].value : null;
        endDate = document.forms["timelog"].elements["endTime["+ i +"]"] ? document.forms["timelog"].elements["endTime["+ i +"]"].value : null;
        deleted = document.forms["timelog"].elements["deleted["+ i +"]"] ? document.forms["timelog"].elements["deleted["+ i +"]"].checked : false;
        if (!deleted){
            interval = computeInterval (startDate, endDate);
            if (interval > 0.0){
                actualHours += Math.round(interval * 10) / 10;
            }
            else if (duration > 0.0){
                actualHours += Math.round(duration * 10) / 10;
            }
       }
    }
    return actualHours;
}

function computeInterval(startDateStr, endDateStr){
    if (startDateStr == null || endDateStr == null)
        return 0.0;
    var startDate, endDate, interval;
    var re = /(\d\d\d\d)-(\d\d)-(\d\d)(.+)/;
    startDateStr = startDateStr.replace(re, "$2/$3/$1 $4");
    endDateStr = endDateStr.replace(re, "$2/$3/$1 $4");
    startDate = new Date (startDateStr);
    endDate = new Date (endDateStr);
    interval = endDate.getTime() - startDate.getTime();
    if (interval > 0.0)
        return interval / 3600000;
    else
        return 0.0;
}

function updateRemainingHours(actualHours){
    var difference;
    var remainingHours;
    difference = lastActualHours - actualHours;
    remainingHours = lastRemainingHours + difference;
    saveRemainingHours(remainingHours);
    if (remainingHours < 0.0){
        remainingHours = 0.0;
        document.getElementById("remainingHoursLabel").style.color = "red";
    }
    else{
        document.getElementById("remainingHoursLabel").style.color = "black";
    }
    document.forms["timelog"].remainingHours.value = remainingHours;
}

function saveRemainingHours(value){
    var val = parseFloat(value);
    if (!isNaN(val))
        lastRemainingHours = val;
}

function saveActualHours(value){
    var val = parseFloat(value);
    if (!isNaN(val))
        lastActualHours = val;
}

function changeRemainingHours(){
    var val = parseFloat(document.forms["timelog"].remainingHours.value);
    if (!isNaN(val))
        lastRemainingHours = val;
}