package com.example.proyecto;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class FragmentoFecha extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    OnFechaSeleccionada f;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //inicializamos la interfaz
        f = (OnFechaSeleccionada) getActivity();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendario = Calendar.getInstance(); //inicializamos el calendario
        int agno = calendario.get(Calendar.YEAR); //año
        int mes = calendario.get(Calendar.MONTH); //mes
        int dia = calendario.get(Calendar.DAY_OF_MONTH); //día

        DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, agno, mes, dia);

        //devolvemos el datePicker con los datos seleccionados
        return dpd;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int agno, int mes, int dia) {
        //Ejecutamos la función de la interfaz
        f.onResultadoFecha(agno, mes, dia);
    }

    //Interfaz
    public interface OnFechaSeleccionada {
        public void onResultadoFecha(int agno, int mes, int dia);
    }
}
