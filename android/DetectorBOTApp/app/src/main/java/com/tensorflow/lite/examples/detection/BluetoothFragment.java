package com.tensorflow.lite.examples.detection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.lite.examples.detection.R;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BluetoothFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BluetoothFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BluetoothFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    String foundedDeviceName, foundedDeviceAddress;
    ImageButton refresh, ibVisible;

    public TextView tvFoundedBtName, tvFoundedBtAddress;
    ListView lvBluetooth;

    Switch btTurn;

    List<String> btNames;
    List<String> btMacAddress;
    ArrayAdapter<String> adapter;


    BluetoothAdapter myBluetooth;
    BluetoothSocket btSocket = null;


    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BluetoothFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BluetoothFragment newInstance(String param1, String param2) {
        BluetoothFragment fragment = new BluetoothFragment();
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
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        btTurn = view.findViewById(R.id.switchBt);

        checkBT();
        btTurn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(!myBluetooth.isEnabled()){
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent,200);

                        //IntentFilter BtIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                        //getActivity().registerReceiver(mBroadcastReceiver1 , BtIntent);
                    }
                }else{
                    if(myBluetooth.isEnabled()){
                        //btNames.clear();
                        //adapter.notifyDataSetChanged();
                        myBluetooth.disable();

                        //IntentFilter BtIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                        //getActivity().registerReceiver(mBroadcastReceiver1 , BtIntent);
                    }

                }
            }
        });



        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 200){
            if(resultCode == RESULT_OK){
                /*Set<BluetoothDevice> pairedDevices = myBluetooth.getBondedDevices();
                for(BluetoothDevice bt : pairedDevices){
                    btNames.add(bt.getName());
                    btMacAddress.add(bt.getAddress());
                }
                adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,btNames);
                lvBluetooth.setAdapter(adapter);
*/
            }else{
                btTurn.setChecked(false);

            }
        }

        if(requestCode == 400){
            Toast.makeText(getContext(), "You are visible now to other devices", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    void checkBT(){
        if(myBluetooth.isEnabled()){
            if(!btTurn.isChecked())    {
                btTurn.setChecked(true);
                /*Set<BluetoothDevice> pairedDevices = myBluetooth.getBondedDevices();
                for(BluetoothDevice bt : pairedDevices){
                    btNames.add(bt.getName());
                    btMacAddress.add(bt.getAddress());
                }
                adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,btNames);
                lvBluetooth.setAdapter(adapter);*/
            }
        }else{
            if(btTurn.isChecked()){
                btTurn.setChecked(false);
            }
        }
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
