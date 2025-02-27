package giswar.secure;

import ca.bc.hnet.moh.ccims.gis.business.GisRecipientsFacadeLocal;
import ca.bc.hnet.moh.ccims.gis.entity.GisRecipients;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Search Utilities
 */
public class SearchUnit {

    private final GisRecipientsFacadeLocal gisRecipientsBean;
 
    private String lastLoadPeriod;

    // maximum number of results that can be returned from the search
    // in order for the search results to display to the user
    private static final int MAX_RESULTS = 5000;
    private final Lock searchLock = new ReentrantLock();

    /**
     * Construct a new Page bean instance. Calls the clear method to reset all
     * values retrieved from the search JSP and database (EJB)
     * @param gisRecipientsBean - the GisRecipientsFacade
     */
    public SearchUnit(GisRecipientsFacadeLocal gisRecipientsBean) {
        this.gisRecipientsBean = gisRecipientsBean;
    }
      
    /**
     * Performs the search against the database
     * @param searchParams - user input search parameters
     * @return List<> Search results
     */
     public List<GisRecipients> searchAction(GisRecipients searchParams) {

        if (!validate(searchParams)) {
            return null;
        }
        
        List<GisRecipients> searchResults; 

        try {

            // Lock use is not beneficial in all scenarios but ensures search action performed from the same
            // browser tab are handled sequentially
            searchLock.lock();

            // Retrieve the load id from the search JSP
            Calendar lastUpdate = null;
            Integer lastUpdateMonth = Integer.parseInt(searchParams.getLoadId());

            if (lastLoadPeriod == null) {
                lastLoadPeriod = gisRecipientsBean.getLastLoadDate();
            }

            if (lastLoadPeriod != null && !lastLoadPeriod.equals("")) {

                int loadYear = Integer.parseInt(lastLoadPeriod.substring(0, 4));
                int loadMonth = Integer.parseInt(lastLoadPeriod.substring(5));

                lastUpdate = new GregorianCalendar(loadYear, loadMonth, 1);
                lastUpdate.add(Calendar.MONTH, -lastUpdateMonth);
            }

            // Performs a count to determine the number of results returned
            int resultCount = gisRecipientsBean.findCount(
                    searchParams.getAccountId(),
                    searchParams.getRcptSurname(),
                    searchParams.getRcptMiddlename(),
                    searchParams.getRcptGivenname(),
                    searchParams.getBirthdate(),
                    lastUpdate);

            // If the number of results returned is greater than the MAX_RESULTS
            // an error message is displayed
            if (resultCount > MAX_RESULTS) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.addMessage(null, new FacesMessage("Too many results "
                        + "returned, please refine your search criteria."));

                return null;
            }

            // Perform the search against the database (EJB) with the supplied
            // search parameters
            searchResults = gisRecipientsBean.find(searchParams.getAccountId(),
                    searchParams.getRcptSurname(),
                    searchParams.getRcptMiddlename(),
                    searchParams.getRcptGivenname(),
                    searchParams.getBirthdate(),
                    lastUpdate);

            // If there are no results returned from the database, display the error
            // message on the search JSP
            if (searchResults == null || searchResults.isEmpty()) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.addMessage(null, new FacesMessage("No search "
                        + "results were found, please refine your search criteria."));
            }

        } finally {
            searchLock.unlock();
        }

        return searchResults;
    }

    /**
     * Display an error message if there isn't at least one search field filled out
     * @return boolean the validation status
     */
     private boolean validate(GisRecipients clonedSearchParams) {
        // At least one of the fields must contain data, verify this is true,
        // if not then throw RuntimeException
        if (notEmpty(clonedSearchParams.getAccountId())
                || notEmpty(clonedSearchParams.getRcptSurname())
                || notEmpty(clonedSearchParams.getRcptMiddlename())
                || notEmpty(clonedSearchParams.getRcptGivenname())
                || notEmpty(clonedSearchParams.getBirthdate())) {
            return true;
        } else {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "At least one "
                    + "search field must contain data. Please refine your "
                    + "search criteria and search again.", null));
            return false;
        }
    }

    private static boolean notEmpty(String s) {
        return (s != null && !s.trim().equals(""));
    }

}
