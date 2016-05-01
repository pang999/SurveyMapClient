package com.powerble.BLEProfileDataParserClasses;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Handler;
import android.os.Parcel;
import android.util.SparseArray;
import com.powerble.BLEConnectionServices.BluetoothLeService;
import com.powerble.CommonUtils.BLELogger;
import com.powerble.CommonUtils.UUIDDatabase;

import java.util.UUID;

public class GlucoseParser {
    private static final int CASE_BEDTIME = 5;
    private static final int CASE_BREAKFAST = 1;
    private static final int CASE_BRUNCH = 7;
    private static final int CASE_CASUAL = 4;
    private static final int CASE_DINNER = 3;
    private static final int CASE_DRINK = 5;
    private static final int CASE_FAST = 3;
    private static final int CASE_GSL_AST = 2;
    private static final int CASE_GSL_CS = 4;
    private static final int CASE_GSL_EARLOBE = 3;
    private static final int CASE_GSL_FINGER = 1;
    private static final int CASE_GSL_SAMPLE = 15;
    private static final int CASE_GT_AP = 6;
    private static final int CASE_GT_AWB = 5;
    private static final int CASE_GT_CP = 2;
    private static final int CASE_GT_CS = 10;
    private static final int CASE_GT_CWB = 1;
    private static final int CASE_GT_ISF = 9;
    private static final int CASE_GT_UP = 8;
    private static final int CASE_GT_UWB = 7;
    private static final int CASE_GT_VP = 4;
    private static final int CASE_GT_VWB = 3;
    private static final int CASE_HCP = 2;
    private static final int CASE_HVNA = 15;
    private static final int CASE_IAI = 3;
    private static final int CASE_LABTEST = 3;
    private static final int CASE_LAI = 4;
    private static final int CASE_LUNCH = 2;
    private static final int CASE_MAJ_HEALTH = 2;
    private static final int CASE_MENSES = 3;
    private static final int CASE_MIN_HEALTH = 1;
    private static final int CASE_NO_HEALTH_ISSUES = 5;
    private static final int CASE_PMI = 5;
    private static final int CASE_POST = 2;
    private static final int CASE_PRE = 1;
    private static final int CASE_RAI = 1;
    private static final int CASE_RESERVED = 0;
    private static final int CASE_SAI = 2;
    private static final int CASE_SELF = 1;
    private static final int CASE_SNACK = 4;
    private static final int CASE_STRESS = 4;
    private static final int CASE_SUPPER = 6;
    private static final int CASE_TVNA = 15;
    private static final int FILTER_TYPE_SEQUENCE_NUMBER = 1;
    private static final int FILTER_TYPE_USER_FACING_TIME = 2;
    private static final int OPERATOR_ALL_RECORDS = 1;
    private static final int OPERATOR_FIRST_RECORD = 5;
    private static final int OPERATOR_GREATER_THEN_OR_EQUAL = 3;
    private static final int OPERATOR_LAST_RECORD = 6;
    private static final int OPERATOR_LESS_THEN_OR_EQUAL = 2;
    private static final int OPERATOR_NULL = 0;
    private static final int OPERATOR_WITHING_RANGE = 4;
    private static final int OP_CODE_ABORT_OPERATION = 3;
    private static final int OP_CODE_DELETE_STORED_RECORDS = 2;
    private static final int OP_CODE_NUMBER_OF_STORED_RECORDS_RESPONSE = 5;
    private static final int OP_CODE_REPORT_NUMBER_OF_RECORDS = 4;
    private static final int OP_CODE_REPORT_STORED_RECORDS = 1;
    private static final int OP_CODE_RESPONSE_CODE = 6;
    private static final int RESPONSE_ABORT_UNSUCCESSFUL = 7;
    private static final int RESPONSE_INVALID_OPERAND = 5;
    private static final int RESPONSE_INVALID_OPERATOR = 3;
    private static final int RESPONSE_NO_RECORDS_FOUND = 6;
    private static final int RESPONSE_OPERAND_NOT_SUPPORTED = 9;
    private static final int RESPONSE_OPERATOR_NOT_SUPPORTED = 4;
    private static final int RESPONSE_OP_CODE_NOT_SUPPORTED = 2;
    private static final int RESPONSE_PROCEDURE_NOT_COMPLETED = 8;
    private static final int RESPONSE_SUCCESS = 1;
    private static final int UNIT_kg = 0;
    private static final int UNIT_kgpl = 0;
    private static final int UNIT_l = 1;
    private static final int UNIT_molpl = 1;
    private static Handler mHandler;
    /*public static SparseArray<GlucoseRecord> mMeasurementRecords;

    static {
        mMeasurementRecords = new SparseArray();
        mHandler = new Handler();
    }

    public static SparseArray<GlucoseRecord> getGlucoseMeasurement(BluetoothGattCharacteristic characteristic) {
        UUID uuid = characteristic.getUuid();
        int offset;
        if (UUIDDatabase.UUID_GLUCOSE_MEASUREMENT.equals(uuid)) {
            int flags = characteristic.getIntValue(17, 0).intValue();
            offset = 0 + 1;
            boolean timeOffsetPresent = (flags & 1) > 0;
            boolean typeAndLocationPresent = (flags & 2) > 0;
            int concentrationUnit = (flags & 4) > 0 ? UNIT_molpl : UNIT_kgpl;
            boolean sensorStatusAnnunciationPresent = (flags & 8) > 0;
            boolean contextInfoFollows = (flags & 16) > 0;
            GlucoseRecord glucoseRecord = new GlucoseRecord(Parcel.obtain());
            glucoseRecord.sequenceNumber = characteristic.getIntValue(18, offset).intValue();
            offset += 2;
            glucoseRecord.time = DateTimeParser.parse(characteristic, offset);
            offset += 7;
            if (timeOffsetPresent) {
                glucoseRecord.timeOffset = characteristic.getIntValue(34, offset).intValue();
                offset += 2;
            }
            if (typeAndLocationPresent) {
                glucoseRecord.glucoseConcentration = characteristic.getFloatValue(50, offset).floatValue();
                glucoseRecord.unit = concentrationUnit;
                int typeAndLocation = characteristic.getIntValue(17, offset + 2).intValue();
                glucoseRecord.type = getType((typeAndLocation & 240) >> 4);
                glucoseRecord.sampleLocation = getLocation(typeAndLocation & 15);
                offset += 3;
            }
            if (sensorStatusAnnunciationPresent) {
                glucoseRecord.status = characteristic.getIntValue(18, offset).intValue();
            }
            mMeasurementRecords.put(glucoseRecord.sequenceNumber, glucoseRecord);
            glucoseRecord.context = contextInfoFollows;
            return mMeasurementRecords;
        }
        if (UUIDDatabase.UUID_GLUCOSE_MEASUREMENT_CONTEXT.equals(uuid)) {
        int     flags = characteristic.getIntValue(17, 0).intValue();
            offset = 0 + 1;
            boolean carbohydratePresent = (flags & 1) > 0;
            boolean mealPresent = (flags & 2) > 0;
            boolean testerHealthPresent = (flags & 4) > 0;
            boolean exercisePresent = (flags & 8) > 0;
            boolean medicationPresent = (flags & 16) > 0;
            int medicationUnit = (flags & 32) > 0 ? UNIT_molpl : UNIT_kgpl;
            boolean hbA1cPresent = (flags & 64) > 0;
            boolean moreFlagsPresent = (flags & 128) > 0;
            int sequenceNumber = characteristic.getIntValue(18, offset).intValue();
            offset += 2;
            GlucoseRecord record = (GlucoseRecord) mMeasurementRecords.get(sequenceNumber);
            if (record == null) {
                Logger.w("Context information with unknown sequence number: " + sequenceNumber);
                return null;
            }
            if (moreFlagsPresent) {
                offset++;
            }
            if (carbohydratePresent) {
                int carbohydrateId = characteristic.getIntValue(17, offset).intValue();
                float carbohydrateUnits = characteristic.getFloatValue(50, offset + 1).floatValue();
                record.carbohydrateId = getCarbohydrate(carbohydrateId);
                record.carbohydrateUnits = carbohydrateUnits + (carbohydrateUnits == 0.0f ? "kg" : "l");
                offset += 3;
            }
            if (mealPresent) {
                record.meal = getMeal(characteristic.getIntValue(17, offset).intValue());
                offset++;
            }
            if (testerHealthPresent) {
                int testerHealth = characteristic.getIntValue(17, offset).intValue();
                int health = testerHealth & 15;
                record.tester = getTester((testerHealth & 240) >> 4);
                record.health = getHealth(health);
                offset++;
            }
            if (exercisePresent) {
                int exerciseDuration = characteristic.getIntValue(18, offset).intValue();
                int exerciseIntensity = characteristic.getIntValue(17, offset + 2).intValue();
                record.exerciseDuration = exerciseDuration + "s";
                record.exerciseIntensity = exerciseIntensity + "%";
                offset += 3;
            }
            if (medicationPresent) {
                int medicationId = characteristic.getIntValue(17, offset).intValue();
                float medicationQuantity = characteristic.getFloatValue(50, offset + 1).floatValue();
                record.medicationId = getMedicationId(medicationId);
                record.medicationQuantity = medicationQuantity;
                record.medicationUnit = medicationUnit == 0 ? "kg" : "l";
                offset += 3;
            }
            if (hbA1cPresent) {
                record.HbA1c = characteristic.getFloatValue(50, offset).floatValue() + "%";
            }
        }
        return mMeasurementRecords;
    }
*/
    public static void onCharacteristicIndicated(BluetoothGattCharacteristic characteristic) {
        int opCode = characteristic.getIntValue(17, UNIT_kgpl).intValue();
        int offset = 0 + 2;
        if (opCode == 5) {
            if (characteristic.getIntValue(18, offset).intValue() > 0) {
                BluetoothGattCharacteristic racpCharacteristic = characteristic;
                setOpCode(racpCharacteristic, UNIT_molpl, UNIT_molpl, new Integer[0]);
                BluetoothLeService.writeCharacteristic(racpCharacteristic);
                return;
            }
            BLELogger.e("No records");
        } else if (opCode == 6) {
            int requestedOpCode = characteristic.getIntValue(17, offset).intValue();
            int responseCode = characteristic.getIntValue(17, RESPONSE_INVALID_OPERATOR).intValue();
            BLELogger.d("Response result for: " + requestedOpCode + " is: " + responseCode);
            switch (responseCode) {
                case UNIT_molpl:
                    BLELogger.e("RESPONSE_SUCCESS");
                case RESPONSE_OP_CODE_NOT_SUPPORTED:
                    BLELogger.e("RESPONSE_SUCCESS");
                case RESPONSE_NO_RECORDS_FOUND:
                    BLELogger.e("RESPONSE_SUCCESS");
                case RESPONSE_ABORT_UNSUCCESSFUL:
                    BLELogger.e("RESPONSE_SUCCESS");
                    BLELogger.e("RESPONSE_UNKOWN");
                case RESPONSE_PROCEDURE_NOT_COMPLETED:
                    BLELogger.e("RESPONSE_SUCCESS");
                    BLELogger.e("RESPONSE_SUCCESS");
                    BLELogger.e("RESPONSE_UNKOWN");
                default:
                    BLELogger.e("RESPONSE_UNKOWN");
            }
        }
    }

    private static void setOpCode(BluetoothGattCharacteristic characteristic, int opCode, int operator, Integer... params) {
        characteristic.setValue(new byte[(((params.length > 0 ? 1 : UNIT_kgpl) + 2) + (params.length * 2))]);
        characteristic.setValue(opCode, 17, UNIT_kgpl);
        int offset = 0 + 1;
        characteristic.setValue(operator, 17, offset);
        offset++;
        if (params.length > 0) {
            characteristic.setValue(UNIT_molpl, 17, offset);
            offset++;
            Integer[] arr$ = params;
            int len$ = arr$.length;
            for (int i$ = UNIT_kgpl; i$ < len$; i$++) {
                characteristic.setValue(arr$[i$].intValue(), 18, offset);
                offset += 2;
            }
        }
    }

    public void getLastRecord(BluetoothGattCharacteristic mRecordAccessControlPointCharacteristic) {
        if (mRecordAccessControlPointCharacteristic != null) {
           // clear();
            BluetoothGattCharacteristic characteristic = mRecordAccessControlPointCharacteristic;
            setOpCode(characteristic, UNIT_molpl, RESPONSE_NO_RECORDS_FOUND, new Integer[0]);
            BluetoothLeService.writeCharacteristic(characteristic);
        }
    }

    public void getAllRecords(BluetoothGattCharacteristic mRecordAccessControlPointCharacteristic) {
        if (mRecordAccessControlPointCharacteristic != null) {
          //  clear();
            BluetoothGattCharacteristic characteristic = mRecordAccessControlPointCharacteristic;
            setOpCode(characteristic, RESPONSE_OPERATOR_NOT_SUPPORTED, UNIT_molpl, new Integer[0]);
            BluetoothLeService.writeCharacteristic(characteristic);
        }
    }

  /*  public void clear() {
        mMeasurementRecords.clear();
    }
*/
    public void deleteAllRecords(BluetoothGattCharacteristic mRecordAccessControlPointCharacteristic) {
        if (mRecordAccessControlPointCharacteristic != null) {
         //   clear();
            BluetoothGattCharacteristic characteristic = mRecordAccessControlPointCharacteristic;
            setOpCode(characteristic, RESPONSE_OP_CODE_NOT_SUPPORTED, UNIT_molpl, new Integer[0]);
            BluetoothLeService.writeCharacteristic(characteristic);
        }
    }

    private static String getType(int type) {
        switch (type) {
            case UNIT_molpl:
                return "Capillary Whole blood";
            case RESPONSE_OP_CODE_NOT_SUPPORTED:
                return "Capillary Plasma";
            case RESPONSE_INVALID_OPERATOR:
                return "Venous Whole blood";
            case RESPONSE_OPERATOR_NOT_SUPPORTED:
                return "Venous Plasma";
            case RESPONSE_INVALID_OPERAND:
                return "Arterial Whole blood";
            case RESPONSE_NO_RECORDS_FOUND:
                return "Arterial Plasma";
            case RESPONSE_ABORT_UNSUCCESSFUL:
                return "Undetermined Whole blood";
            case RESPONSE_PROCEDURE_NOT_COMPLETED:
                return "Undetermined Plasma";
            case RESPONSE_OPERAND_NOT_SUPPORTED:
                return "Interstitial Fluid (ISF)";
            case CASE_GT_CS:
                return "Control Solution";
            default:
                return "Reserved for future use (" + type + ")";
        }
    }

    private static String getLocation(int location) {
        switch (location) {
            case UNIT_molpl:
                return "Finger";
            case RESPONSE_OP_CODE_NOT_SUPPORTED:
                return "Alternate Site Test (AST)";
            case RESPONSE_INVALID_OPERATOR:
                return "Earlobe";
            case RESPONSE_OPERATOR_NOT_SUPPORTED:
                return "Control solution";
            case CASE_TVNA:
                return "Value not available";
            default:
                return "Reserved for future use (" + location + ")";
        }
    }

    private static String getCarbohydrate(int id) {
        switch (id) {
            case UNIT_molpl:
                return "Breakfast";
            case RESPONSE_OP_CODE_NOT_SUPPORTED:
                return "Lunch";
            case RESPONSE_INVALID_OPERATOR:
                return "Dinner";
            case RESPONSE_OPERATOR_NOT_SUPPORTED:
                return "Snack";
            case RESPONSE_INVALID_OPERAND:
                return "Drink";
            case RESPONSE_NO_RECORDS_FOUND:
                return "Supper";
            case RESPONSE_ABORT_UNSUCCESSFUL:
                return "Brunch";
            default:
                return "Reserved for future use (" + id + ")";
        }
    }

    private static String getMeal(int id) {
        switch (id) {
            case UNIT_molpl:
                return "Preprandial (before meal)";
            case RESPONSE_OP_CODE_NOT_SUPPORTED:
                return "Postprandial (after meal)";
            case RESPONSE_INVALID_OPERATOR:
                return "Fasting";
            case RESPONSE_OPERATOR_NOT_SUPPORTED:
                return "Casual (snacks, drinks, etc.)";
            case RESPONSE_INVALID_OPERAND:
                return "Bedtime";
            default:
                return "Reserved for future use (" + id + ")";
        }
    }

    private static String getTester(int id) {
        switch (id) {
            case UNIT_molpl:
                return "Self";
            case RESPONSE_OP_CODE_NOT_SUPPORTED:
                return "Health Care Professional";
            case RESPONSE_INVALID_OPERATOR:
                return "Lab test";
            case RESPONSE_OPERATOR_NOT_SUPPORTED:
                return "Casual (snacks, drinks, etc.)";
            case CASE_TVNA:
                return "Tester value not available";
            default:
                return "Reserved for future use (" + id + ")";
        }
    }

    private static String getHealth(int id) {
        switch (id) {
            case UNIT_molpl:
                return "Minor health issues";
            case RESPONSE_OP_CODE_NOT_SUPPORTED:
                return "Major health issues";
            case RESPONSE_INVALID_OPERATOR:
                return "During menses";
            case RESPONSE_OPERATOR_NOT_SUPPORTED:
                return "Under stress";
            case RESPONSE_INVALID_OPERAND:
                return "No health issues";
            case CASE_TVNA:
                return "Health value not available";
            default:
                return "Reserved for future use (" + id + ")";
        }
    }

    private static String getMedicationId(int id) {
        switch (id) {
            case UNIT_molpl:
                return "Rapid acting insulin";
            case RESPONSE_OP_CODE_NOT_SUPPORTED:
                return "Short acting insulin";
            case RESPONSE_INVALID_OPERATOR:
                return "Intermediate acting insulin";
            case RESPONSE_OPERATOR_NOT_SUPPORTED:
                return "Long acting insulin";
            case RESPONSE_INVALID_OPERAND:
                return "Pre-mixed insulin";
            default:
                return "Reserved for future use (" + id + ")";
        }
    }
}
