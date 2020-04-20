package com.senac.renato.control;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.senac.renato.model.dao.PaisDao;
import com.senac.renato.model.vo.Pais;
import com.senac.renato.view.PaisActivity;

import java.sql.SQLException;
import java.util.List;

public class paisControl {
    private PaisActivity activity;

    //Para o ListView
    private ArrayAdapter<Pais> adapterPais;
    private List<Pais> listPais;

    private Pais pais;

    //Criação dos DAOS
    private PaisDao paisDao;

    public paisControl(PaisActivity activity) {
        this.activity = activity;
        paisDao = new PaisDao(this.activity);
        configListViewPais();
    }


    private void configListViewPais() {
        //Elementos da lista
        try {
            listPais = paisDao.getDao().queryForAll();
            adapterPais = new ArrayAdapter<>(
                    activity,
                    android.R.layout.simple_list_item_1,
                    listPais
            );
            activity.getViewPaises().setAdapter(adapterPais);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        addCliqueLongoViewPaises();
        addCliqueCurtoViewPaises();
    }

    private void addCliqueLongoViewPaises() {
        activity.getViewPaises().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                pais = adapterPais.getItem(position);
                confirmarExclusaoAction(pais);
                return true;
            }
        });
    }

    public void addCliqueCurtoViewPaises() {
        activity.getViewPaises().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                pais = adapterPais.getItem(position);

                AlertDialog.Builder alerta = new AlertDialog.Builder(activity);
                alerta.setTitle("Pais");
                alerta.setMessage(pais.toString());
                alerta.setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pais = null;
                    }
                });
                alerta.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        popularFormAction(pais);
                    }
                });
                alerta.show();
            }
        });
    }

    private Pais getPaisForm() {
        Pais pais = new Pais();
        pais.setNome(activity.getEditNomePais().getText().toString());
        return pais;
    }


    private void limparForm() {
        activity.getEditNomePais().setText("");
    }

    public void popularFormAction(Pais p) {
        activity.getEditNomePais().setText(p.getNome());
    }

    public void confirmarExclusaoAction(final Pais p) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(activity);
        alerta.setTitle("Excluindo país");
        alerta.setMessage("Deseja realmente excluir o país " + p.getNome() + "?");
        alerta.setIcon(android.R.drawable.ic_menu_delete);
        alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pais = null;
            }
        });
        alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    paisDao.getDao().delete(p);
                    adapterPais.remove(p);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                pais = null;
            }
        });
        alerta.show();
    }


    private void cadastrar() {
        Pais pais = getPaisForm();
        try {
            int res = paisDao.getDao().create(pais); //Envia para o banco de dados
            adapterPais.add(pais); //Atualiza no ListView

            if (res > 0) {
                Toast.makeText(activity, "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "Tente novamente em breve", Toast.LENGTH_SHORT).show();
            }

            //LOG
            Log.i("Testando", "Cadastrou");
            Toast.makeText(activity, "Id:" + pais.getId(), Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void editar(Pais newPais) {
        pais.setNome(newPais.getNome());
        try {
            adapterPais.notifyDataSetChanged(); //Atualiza no view
            int res = paisDao.getDao().update(pais); //Editar no banco de dados
            if (res > 0) {
                Toast.makeText(activity, "Sucesso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "Tente mais tarde", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void salvarAction() {
        if (pais == null) {
            cadastrar();
        } else {
            editar(getPaisForm());
        }

        pais = null;
        limparForm();
    }
}

