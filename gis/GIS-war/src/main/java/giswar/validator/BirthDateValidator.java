/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package giswar.validator;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
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
