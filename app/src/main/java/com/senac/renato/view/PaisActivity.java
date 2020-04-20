package com.senac.renato.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.senac.renato.R;
import com.senac.renato.control.paisControl;


public class PaisActivity extends AppCompatActivity {
    private EditText editNomePais;
    private ListView viewPaises;
    private Button btnSalvar;
    private paisControl control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pais);
        initialize();
        control = new paisControl(this);
    }

    private void initialize() {
        viewPaises = findViewById(R.id.ViewPais);
        editNomePais = findViewById(R.id.editNomePais);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.salvarAction();
            }
        });
    }

    public EditText getEditNomePais() {
        return editNomePais;
    }

    public void setEditNomePais(EditText editNomePais) {
        this.editNomePais = editNomePais;
    }

    public ListView getViewPaises() {
        return viewPaises;
    }

    public void setViewPaises(ListView viewPaises) {
        this.viewPaises = viewPaises;
    }

    public Button getBtnSalvar() {
        return btnSalvar;
    }

    public void setBtnSalvar(Button btnSalvar) {
        this.btnSalvar = btnSalvar;
    }
}
