package event;

import com.cele.immo.annotation.CascadeSave;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

@Data
@Slf4j
public class CascadeCallback implements ReflectionUtils.FieldCallback {
    private Object source;
    private MongoOperations mongoOperations;

    CascadeCallback(final Object source, final MongoOperations mongoOperations) {
        this.source = source;
        this.mongoOperations = mongoOperations;

    }

    @Override
    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
        ReflectionUtils.makeAccessible(field);

        if (field.isAnnotationPresent(DBRef.class) && field.isAnnotationPresent(CascadeSave.class)) {
            final Object fieldValue = field.get(getSource());

            if (fieldValue != null) {
                final FieldCallback callback = new FieldCallback();

                ReflectionUtils.doWithFields(fieldValue.getClass(), callback);
                getMongoOperations().save(fieldValue);
            }
        }

    }
}
