// source for CGI JSF Framework

$(document).click(function(event) { 
    if(!$(event.target).closest('#detailedResults').length) {
        if($('#detailedResults').is(":visible")) {
            PF('detailedResults').hide();
        }
    }        
});


function confirmExit() {
    return confirm("Please confirm you want to sign out. \nThis will also end all other active Keycloak, PHSA, or SiteMinder sessions you have open.");
}

// Session Management
const TIMEOUT = 1920000; // 32 MINS slightly longer than KC session timeout, 1 minute less than timeout in web.xml
const ACTIVITY_CHECK_INTERVAL = 60000; // 1 min
const MAX_TIME_REMAINING_BEFORE_GROWL = 120000; // 2 min - growl will be shown when this much time is remaining in the session

$(document).ready(function() {
    $.active = false;
    $.growl = false;
    $.elapsed = 0;
    $('body').bind('click keypress', function() { $.active = true; });
    checkActivity();
    setInterval(checkActivity, ACTIVITY_CHECK_INTERVAL); // function, interval
});

function checkActivity() {
    if ($.active) {
        $.elapsed = 0;
        $.active = false;
        $.growl = false;
        PF('timeoutWarning').removeAll();
        extendSession(); // p:remoteCommand in gis.xhtml
    }
    if ($.elapsed < TIMEOUT) {
        $.elapsed += ACTIVITY_CHECK_INTERVAL;

        if ($.elapsed > (TIMEOUT - MAX_TIME_REMAINING_BEFORE_GROWL) && $.growl === false) {
            PF('timeoutWarning').show([{
                "summary":"Your session is about to expire",
                "detail":"Please continue using the application to extend your session",
                "severity":"warn"}]);
            $.growl = true;
        }
    } else {
        endSession().then(() => location.reload());  
    }
}

