package dst.ass2.di.impl;

import dst.ass2.di.IInjectionController;
import dst.ass2.di.InjectionException;
import dst.ass2.di.annotation.Component;
import dst.ass2.di.annotation.ComponentId;
import dst.ass2.di.annotation.Inject;
import dst.ass2.di.model.ScopeType;
import dst.ass2.di.util.InjectionUtils;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


/**
 * Created by amra.
 */
public class InjectionControler implements IInjectionController {

    private Map<Class<?>, Object> initSingletons;
    private final AtomicLong nextId = new AtomicLong(0);

    //concurrenthaspmapp sluzi da se automatski sinkronizuje (samo sinegtons tharedsafes)
    public InjectionControler() {
        initSingletons = new ConcurrentHashMap<>();

    }

    @Override
    public void initialize(Object obj) throws InjectionException {

        if(!isComponent(obj.getClass()))
            throw new InjectionException("the object doesn't hava a component annotation!");


        //u singletonmap provjeri da li je singelton
        if (isSingleton(obj.getClass()) && initSingletons.containsKey(obj.getClass())) {
            obj = initSingletons.get(obj.getClass());
        } else if (isSingleton(obj.getClass())) {
            initSingletons.put(obj.getClass(), obj);
        }


        if (isSingleton(obj.getClass()) && isInitialized(obj)) throw new InjectionException("Already initialized");

        // nadji filds od superclass fields, uzimamo sve declare fields i ubacimo u listu
        // kad zavrsimo uzimamo sljedecu superclassu i radimo isto
        ArrayList<Field> allFields = new ArrayList<>();
        Class superclass = obj.getClass();
        while (superclass != null) {
            for (Field f : superclass.getDeclaredFields()) {
                allFields.add(f);
            }
            superclass = superclass.getSuperclass();
        }


        // set fields
        // ako su fields takodjer i components onda jos jendom initialize za odgovoarajuc field pozvati
        for(Field field: allFields) {

            //ako je injection tu
            if (field.isAnnotationPresent(Inject.class)){

                field.setAccessible(true);
                Inject injectann = field.getAnnotation(Inject.class);

                // specific type uzeti kad je on void uzimas kao fallback fieldtype
                // specify a concrete subtype that should be instantiated (specificType)
                Class<?> type = injectann.specificType();
                if (type == Void.class) type = field.getType();

                //ako singelton
                try {
                    Object instance;
                    if (isSingleton(type)) {
                        instance = getSingletonInstance(type);
                    } else {

                        instance = type.newInstance();
                        initialize(instance);
                    }

                    field.set(obj,instance);

                } catch (Exception e) {
                    //ako nije required onda nastavi

                    if (injectann.required()) throw new InjectionException(e);
                }
            }
        }


        //get id
        Long id = nextId.getAndIncrement();

        // all objects within an inheritance hierarchy must have the same id
        Set<Field> allfields = InjectionUtils.getIdsOfHierarchy(obj).keySet();

        for(Field field: allfields){
            if (field.getType() != Long.class) throw new InjectionException(" id variable has the wrong type");
            field.setAccessible(true);
            try {
                field.set(obj, id);
            } catch (IllegalAccessException e) {
                throw new InjectionException(e);
            }
        }
    }


    @Override
    public <T> T getSingletonInstance(Class<T> clazz) throws InjectionException {
        if (!isSingleton(clazz)) {
            throw new InjectionException(clazz + " is not a singleton!");
        }

        if (!initSingletons.containsKey(clazz)) {

            try {
                Object object = clazz.newInstance();
                //za snimanje u  singeltonmap, da imamo snimljeno
                initSingletons.put(clazz, object);
                initialize(object);


            } catch (InstantiationException e) {
                throw new InjectionException(e);
            } catch (IllegalAccessException e) {
                throw new InjectionException(e);
            }
        }

        return (T)initSingletons.get(clazz);
    }

    private boolean isSingleton(Class<?> clazz) {
        for (Annotation annotation : clazz.getDeclaredAnnotations()) {

            if (annotation.annotationType() == Component.class) {
                Component component = (Component) annotation;
                boolean issingelton = component.scope() == ScopeType.SINGLETON;
                return issingelton;
            }
        }

        return false;
    }


    private boolean isPrototype(Class<?> clazz) {
        for (Annotation annotation : clazz.getDeclaredAnnotations()) {

            if (annotation.annotationType() == Component.class) {
                Component component = (Component) annotation;
                boolean issingelton = component.scope() == ScopeType.PROTOTYPE;
                return issingelton;
            }
        }

        return false;
    }


    private boolean isComponent(Class<?> clazz) {
        for (Annotation annotation : clazz.getDeclaredAnnotations()) {
            if (annotation.annotationType() == Component.class) {
                return true;
            }
        }

        return false;
    }


    private boolean isInitialized(Object object) throws InjectionException{
        Field id = null;

        for (Field field : object.getClass().getDeclaredFields()) {
            Annotation processedAnnotation = null;
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation.annotationType() == ComponentId.class) {
                    id = field;
                    break;
                }
            }
            if (id != null) break;
        }

        if (id == null) {
            throw new InjectionException(object+" does not have a ComponentId");
        } else {
            boolean accessible = id.isAccessible();
            id.setAccessible(true);
            try {
                // uzima wert za feld od Objekt object
                return id.get(object) != null;
            } catch (IllegalAccessException e) {
                throw new InjectionException(e);
            } finally {
                // vraca wert kakav je bio prije
                id.setAccessible(accessible);
            }
        }
    }

}