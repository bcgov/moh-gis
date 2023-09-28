/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package giswar.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import jakarta.inject.Named;

/**
 *
 * @author trevor.schiavone
 */
@FacesValidator
@Named("BirthDateValidator")
public class BirthDateValidator implements Validator {
    
   @Override
   public void validate(FacesContext facesContext, UIComponent component, Object value) throws ValidatorException {
       if (value == null || value.toString().equals("")) {
           return;
       }
       if (Integer.valueOf(value.toString().substring(5)) > 12 || Integer.valueOf(value.toString().substring(5)) == 0) {
           throw new ValidatorException(
                    new FacesMessage( FacesMessage.SEVERITY_ERROR, "Error: Birth Date must be a valid date with the format yyyy-MM", null)
           );
       }
   }
}
