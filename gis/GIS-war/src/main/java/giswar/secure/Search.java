package giswar.secure;

import ca.bc.hnet.moh.ccims.gis.business.GisAccessLogFacadeLocal;
import ca.bc.hnet.moh.ccims.gis.business.GisRecipientsFacadeLocal;
import ca.bc.hnet.moh.ccims.gis.entity.GisAccessLog;
import ca.bc.hnet.moh.ccims.gis.entity.GisRecipients;
import com.cgi.assm.faces.utils.UserDetails;
import org.primefaces.event.SelectEvent;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import jakarta.inject.Named;


/**
 * This is the main class of the GIS application. All actions are routed
 * through this class.
 *
 * @version Search.java
 */
@Named("Search")
@ViewScoped
public class Search implements Serializable {

    @EJB
    private GisRecipientsFacadeLocal gisRecipientsBean;    // Search parameters object
    @EJB
    private GisAccessLogFacadeLocal accessLogBean;
    
    private SelectItem[] loadPeriods;    
    private String lastLoadPeriod = null; 
        
    private GisRecipients recipientSearchParams;
    private List<GisRecipients> recipientSearchResults;
    private GisRecipients selectedRecipient;

    private LocalDate birthdateCal = LocalDate.of(1960, Month.JANUARY, 1);
    private String birthdate;
    
    private SearchUnit searchUnit;

    @PostConstruct
    public void init() {
        
        lastLoadPeriod = gisRecipientsBean.getLastLoadDate();
        
        loadPeriods = new SelectItem[]{
            new SelectItem(1, "Current"),
            new SelectItem(3, "Last 3 Months"),
            new SelectItem(6, "Last 6 Months"),
            new SelectItem(12, "Last 12 Months")
        };
        
        recipientSearchParams = new GisRecipients();
        searchUnit = new SearchUnit(gisRecipientsBean);
    }

    // this is a hacky workout around to deal with the fact primefaces calendar breaks on dates with out all of day,month,year
    // we set a second input to a second birthdate field that is formatted and hide the actual calendar field
    public void onDateSelect(SelectEvent event) {
        this.birthdate = DateTimeFormatter.ofPattern("yyyy-MM").format(birthdateCal);
    }

    public void searchGisRecipients() {
        recipientSearchParams.setBirthdate(birthdate.replace("-", ""));
        recipientSearchResults = searchUnit.searchAction(recipientSearchParams);
    }
    
    public void clearSearch() {
        birthdate = null;
        birthdateCal = LocalDate.of(1960, Month.JANUARY, 1);
        recipientSearchParams = new GisRecipients();
    }
    
    /*
    * Used when clearing the results but maintaining search parameters
    */
    public void clearResults() {
        recipientSearchResults.clear();
    }
    
    /*
    * Set the selected GIS recipient and log the fact that the user is looking 
    * at detailed results
    */
    public void getDetails(GisRecipients selectedRecipient) {
        this.selectedRecipient = selectedRecipient;
        logUserAccess(selectedRecipient);    
    }
    
    /**
     * Logs user access to a record
     * @param selectedRecipient - the selected record
     */
    private void logUserAccess(GisRecipients selectedRecipient) {

        GisAccessLog result = new GisAccessLog();

        if (selectedRecipient != null && selectedRecipient.getId() != null) {

            UserDetails ud = new UserDetails();

            result.setRcptId(selectedRecipient.getId().toBigInteger());
            result.setIpAddress(ud.getUserIPAddress());
            result.setModule("GIS_DETAILS");

            //If the ip address is loopback then it's the developer workstation, 
            //otherwise fill in UNKNOWN
            String userName = ud.getUserName();

            if (result.getIpAddress().equals("127.0.0.1")) {
                userName = "DEV";
            } else {
                if (userName == null || userName.equals("")) {
                    userName = "UNKNOWN";
                }
            }
            result.setUserId(userName);
        }
        accessLogBean.create(result);
    }
    
    /*
    * Getters and Setters
    */
    public SelectItem[] getLoadPeriods() {
        return loadPeriods;
    }
    
    public String getLastLoadPeriod() {
        return lastLoadPeriod;
    }

    public GisRecipients getRecipientSearchParams() {
        return recipientSearchParams;
    }

    public void setRecipientSearchParams(GisRecipients recipientSearchParams) {
        this.recipientSearchParams = recipientSearchParams;
    }

    public List<GisRecipients> getRecipientSearchResults() {
        return recipientSearchResults;
    }

    public GisRecipients getSelectedRecipient() {
        return selectedRecipient;
    }

    public void setSelectedRecipient(GisRecipients selectedRecipient) {
        this.selectedRecipient = selectedRecipient;
    }

    public String getBirthdate() {
        return birthdate;
    }
    
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public LocalDate getBirthdateCal() {
        return birthdateCal;
    }

    public void setBirthdateCal(LocalDate birthdateCal) {
        this.birthdateCal = birthdateCal;
    }

}