package com.example.mediccompanion.Validation;

import android.text.TextUtils;
import android.widget.EditText;

public class FieldsValidation {
    private EditText drugName, quantity, amountPerDosage,
            numberOfDosagesDaily;

    public FieldsValidation(EditText drugName, EditText quantity,
                            EditText amountPerDosage, EditText numberOfDosagesDaily) {
        this.drugName = drugName;
        this.quantity = quantity;
        this.amountPerDosage = amountPerDosage;
        this.numberOfDosagesDaily = numberOfDosagesDaily;
    }

    public boolean invalidRegistrationInputFields(String drugNameStr, String quantityStr,
                                                  String amount, String dosages) {
        //checking for empty input fields
        // fullnames, email, phonenumber,password,confirmpassword;
        //    private String usernames, useremail, userphone, userpassword, userconpassword;
        if(TextUtils.isEmpty(drugNameStr)){
            drugName.setError("Drug name required!");
            drugName.requestFocus();
            return  true;
        }
        if(TextUtils.isEmpty(quantityStr)){
            quantity.setError("Quantity required!");
            quantity.requestFocus();
            return  true;
        }
        if(TextUtils.isEmpty(amount)){
            amountPerDosage.setError("Amount per dosage Required!");
            amountPerDosage.requestFocus();
            return  true;
        }
        if(TextUtils.isEmpty(dosages)){
            numberOfDosagesDaily.setError("Number of dosages daily required!");
            numberOfDosagesDaily.requestFocus();
            return  true;
        }
        return false;
    }
}
