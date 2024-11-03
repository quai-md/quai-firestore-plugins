package io.cdap.plugin.gcp.firestore.source.util;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.firestore.v1.Value;
import com.google.protobuf.util.JsonFormat;

import java.lang.reflect.Field;
import java.util.Map;

import com.google.gson.Gson;


public class FirestoreUtils {

    /**
     * Retrieves the raw fields map from DocumentSnapshot using reflection.
     *
     * @param documentSnapshot The DocumentSnapshot instance.
     * @return The raw fields as a Map<String, Value>.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Value> getRawFields(DocumentSnapshot documentSnapshot) {
        try {
            Field fieldsField = DocumentSnapshot.class.getDeclaredField("fields");
            fieldsField.setAccessible(true);
            return (Map<String, Value>) fieldsField.get(documentSnapshot);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access raw fields from DocumentSnapshot", e);
        }
    }

    /**
     * Converts a Firestore Value to a string representation.
     * For complex types (RECORD, ARRAY, MAP), it produces a JSON-like format.
     *
     * @param value The Firestore Value to stringify.
     * @return The stringified representation.
     */
    public static String stringifyFirestoreValue(Value value) {
        try {
            return JsonFormat.printer().print(value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to stringify Firestore Value", e);
        }
    }
}