package com.example.myapplication;

public class ListaAlunosActivity {
    package br.edu.unidavi.bsn.activities;

import java.util.List;
import com.example.listagemdealunos.R;
import br.edu.unidavi.bsn.dao.AlunoDAO;
import br.edu.unidavi.bsn.model.Aluno;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

    public class ListaAlunosActivity extends Activity {

        private ListView listaAlunos;

        protected Aluno alunoSelecionado;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            listaAlunos = (ListView) findViewById(R.id.lista_alunos);
            registerForContextMenu(listaAlunos);
            listaAlunos.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapter, View view,
                                        int position, long id) {

                    Intent edicao = new Intent(ListaAlunosActivity.this,
                            FormularioActivity.class);

                    Aluno alunoSelecionado = (Aluno) listaAlunos
                            .getItemAtPosition(position);

                    edicao.putExtra(Extras.ALUNO_SELECIONADO, alunoSelecionado);
                    startActivity(edicao);

                }
            });

            listaAlunos.setOnItemLongClickListener(new OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView<?> adapter, View view,
                                               int posicao, long id) {

                    alunoSelecionado = (Aluno) adapter.getItemAtPosition(posicao);

                    return false;
                }
            });

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_principal, menu);

            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_novo:

                    Intent intent = new Intent(ListaAlunosActivity.this,
                            FormularioActivity.class);

                    startActivity(intent);

                    return false;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        @Override
        protected void onResume() {

            this.carregaLista();
            super.onResume();
        }

        private void carregaLista() {

            List<Aluno> alunos;
            AlunoDAO dao = new AlunoDAO(this);
            alunos = dao.getLista();
            dao.close();

            ListaAlunosAdapter adapter = new ListaAlunosAdapter(this, alunos);

            listaAlunos.setAdapter(adapter);

            listaAlunos = (ListView) findViewById(R.id.lista_alunos);
            listaAlunos.setAdapter(adapter);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenuInfo menuInfo) {

            MenuItem ligar = menu.add("Ligar");
            Intent intentLigar = new Intent(Intent.ACTION_CALL);
            intentLigar.setData(Uri.parse("tel:"+alunoSelecionado.getTelefone()));
            ligar.setIntent(intentLigar);

            MenuItem sms = menu.add("Enviar SMS");
            Intent intentSms = new Intent(Intent.ACTION_VIEW);
            intentSms.setData(Uri.parse("sms:"+alunoSelecionado.getTelefone()));
            intentSms.putExtra("sms_body", "Mensagem");
            sms.setIntent(intentSms);

            MenuItem acharNoMapa = menu.add("Achar no mapa");
            Intent intentMapa = new Intent(Intent.ACTION_VIEW);
            intentMapa.setData(Uri.parse("geo:0,0?z=14&q="+alunoSelecionado.getEndereco()));
            acharNoMapa.setIntent(intentMapa);

            MenuItem site = menu.add("Navegar no site");
            Intent intentSite = new Intent(Intent.ACTION_VIEW);
            String http = alunoSelecionado.getSite().contains("http://")?"":"http://";
            intentSite.setData(Uri.parse(http+alunoSelecionado.getSite()));
            site.setIntent(intentSite);

            MenuItem deletar = menu.add("Deletar");
            deletar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    deletaAluno();
                    carregaLista();
                    return false;
                }
            });

            MenuItem email = menu.add("Enviar E-mail");

            Intent intentEmail = new Intent(Intent.ACTION_SEND);
            intentEmail.setType("message/rtc822");
            intentEmail.putExtra(Intent.EXTRA_EMAIL, new String[] {"erichegt@yahoo.com.br"});
            intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Testando subject do email");
            intentEmail.putExtra(Intent.EXTRA_TEXT, "Testando corpo do email");
            email.setIntent(intentEmail);

            super.onCreateContextMenu(menu, v, menuInfo);
        }

        public void deletaAluno() {
            AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
            dao.deletar(alunoSelecionado);
            dao.close();

            carregaLista();
        }
    }
]
