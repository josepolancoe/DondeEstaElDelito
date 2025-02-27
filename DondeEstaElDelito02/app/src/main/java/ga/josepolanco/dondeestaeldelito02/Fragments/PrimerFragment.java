package ga.josepolanco.dondeestaeldelito02.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ga.josepolanco.dondeestaeldelito02.Clases.Denuncias;
import ga.josepolanco.dondeestaeldelito02.Noticia.DenunciasAdaptador;
import ga.josepolanco.dondeestaeldelito02.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PrimerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PrimerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrimerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerViewNoticias;
    List<Denuncias> denuncias;
    DenunciasAdaptador denunciasAdaptador;
    DatabaseReference mRootReference;

    public PrimerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrimerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrimerFragment newInstance(String param1, String param2) {
        PrimerFragment fragment = new PrimerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_primer, container, false);

        denuncias = new ArrayList<>();
        recyclerViewNoticias = vista.findViewById(R.id.f1_recycler);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerViewNoticias.setLayoutManager(mLayoutManager);
        denunciasAdaptador = new DenunciasAdaptador(denuncias);
        recyclerViewNoticias.setAdapter(denunciasAdaptador);

        database.getReference().child("Denuncias").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                denuncias.removeAll(denuncias);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Denuncias denuncias1 = snapshot.getValue(Denuncias.class);
                    denuncias.add(denuncias1);
                }
                denunciasAdaptador.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        mRootReference = FirebaseDatabase.getInstance().getReference();
//        mRootReference.child("Denuncias").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
////                denuncias.removeAll(denuncias);
//
//                for(final DataSnapshot snapshot : dataSnapshot.getChildren()){
//
//                    mRootReference.child("Denuncias").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            Denuncias denuncias1 = snapshot.getValue(Denuncias.class);
//                            String nombre = denuncias1.getNombre();
//                            String ubicacion = denuncias1.getUbicacion();
//                            String fecha = denuncias1.getFecha();
//                            String tipo_delito = denuncias1.getTipo_delito();
//                            String descripcion = denuncias1.getDescripcion();
//
//                            denuncias.add(denuncias1);
//
//                            Log.e("nombre:",""+nombre);
//                            Log.e("ubicacion:",""+ubicacion);
//                            Log.e("fecha:",""+fecha);
//                            Log.e("tipo_delito:",""+tipo_delito);
//                            Log.e("descripcion:",""+descripcion);
////                            Log.e("Datos:",""+snapshot.getValue());
//                        }
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//                denunciasAdaptador.notifyDataSetChanged();
//
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        return vista;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            final ProgressDialog progress = new ProgressDialog(getContext());
            progress.setTitle("Sección Denuncias");
            progress.setMessage("Descargando Incidentes...");
            progress.show();
            progress.setCancelable(false);
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    progress.cancel();
                }
            };
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 5000);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
