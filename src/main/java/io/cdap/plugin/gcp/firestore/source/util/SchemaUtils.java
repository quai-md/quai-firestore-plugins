package io.cdap.plugin.gcp.firestore.source.util;

import io.cdap.cdap.api.data.schema.Schema;

import java.util.ArrayList;
import java.util.List;

public class SchemaUtils {
    /**
     * Adjusts the schema, converting RECORD, ARRAY, and MAP fields to STRING types.
     * @param originalSchema The original schema.
     * @return A new schema with updated field types.
     */
    public static Schema adjustSchemaForStringifiedFields(Schema originalSchema) {
        List<Schema.Field> adjustedFields = new ArrayList<>();

        assert originalSchema.getFields() != null;
        for (Schema.Field field : originalSchema.getFields()) {
            Schema fieldSchema = field.getSchema();
            boolean isNullable = fieldSchema.isNullable();
            Schema nonNullableSchema = isNullable ? fieldSchema.getNonNullable() : fieldSchema;
            Schema.Type fieldType = nonNullableSchema.getType();

            // Convert RECORD, ARRAY, or MAP types to STRING
            if (fieldType == Schema.Type.RECORD || fieldType == Schema.Type.ARRAY || fieldType == Schema.Type.MAP) {
                Schema stringSchema = Schema.of(Schema.Type.STRING);
                adjustedFields.add(Schema.Field.of(field.getName(), isNullable ? Schema.nullableOf(stringSchema) : stringSchema));
            } else {
                // Preserve the original field schema
                adjustedFields.add(field);
            }
        }
        assert originalSchema.getRecordName() != null;
        return Schema.recordOf(originalSchema.getRecordName(), adjustedFields);
    }
}
