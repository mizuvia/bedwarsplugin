package main;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import java.util.logging.Logger;

public class Data {

    private ByteArrayDataInput in;
    private ByteArrayDataOutput own;
    private ByteArrayDataOutput out;

    public Data(){
        this.own = ByteStreams.newDataOutput();
    }

    public ByteArrayDataInput getInputData() { return in; }

    public void setInputData(ByteArrayDataInput in) { this.in = in; }

    public ByteArrayDataOutput getOutputData() { return out; }

    public void setOutputData(ByteArrayDataOutput out) { this.out = out; }

    public void addOwnData(String data) {
        own.writeUTF(data);
    }

    public String getOwnData() {
        ByteArrayDataInput dataBytes = ByteStreams.newDataInput(this.own.toByteArray());
        String data = dataBytes.readUTF();
        this.own = ByteStreams.newDataOutput();
        try {
            while(true){
                String cycle = dataBytes.readUTF();
                own.writeUTF(cycle);
            }
        } catch(Exception e){
            Logger.getLogger("").info("Reached out of the stream!");
            return data;
        }
    }

}
