package reger.core;

/**
 * A standard validation exception.
 */

public class EmailSendException extends Throwable{

    private String[] emailSendErrors = new String[0];

    public EmailSendException(){

    }

    public EmailSendException(String validationError){
        addError(validationError);
    }

    public String getErrorsAsSingleString(){
        StringBuffer mb = new StringBuffer();
        for (int i = 0; i < emailSendErrors.length; i++) {
            String validationError = emailSendErrors[i];
            mb.append(validationError + "<br>");
        }
        return mb.toString();
    }

    public void addErrorsFromAnotherException(EmailSendException errors){
        for (int i = 0; i < errors.getErrors().length; i++) {
            addError(errors.getErrors()[i]);
        }
    }

    public String[] getErrors(){
        return emailSendErrors;
    }

    public void addError(String error){
        if (emailSendErrors ==null){
            emailSendErrors = new String[0];
        }
        emailSendErrors = Util.addToStringArray(emailSendErrors, error);
    }

}
