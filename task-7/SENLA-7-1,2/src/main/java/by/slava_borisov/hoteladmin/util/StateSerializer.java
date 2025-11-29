package by.slava_borisov.hoteladmin.util;

import by.slava_borisov.hoteladmin.model.HotelSystem;

import java.io.*;

public class StateSerializer {
    private static final String STATE_FILE = "data/state.dat";

    public static HotelSystem loadState() {
        File file = new File(STATE_FILE);

        if(!file.exists()) {
            return new HotelSystem();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (HotelSystem) ois.readObject();
        } catch (Exception e) {
            return new HotelSystem();
        }
    }

    public static void saveState(HotelSystem hotelSystem) {
        File file = new File(STATE_FILE);

        file.getParentFile().mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(hotelSystem);
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}
