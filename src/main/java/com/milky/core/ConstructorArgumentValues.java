package com.milky.core;

import java.util.*;

/**
 * Holder for constructor argument values
 * @author Zheng Chenyang
 * @since 2018.3.18
 */
public class ConstructorArgumentValues {

    private final Map<Integer, ValueHolder> indexedArgumentValues = new LinkedHashMap<Integer, ValueHolder>();

    public static class ValueHolder{
        private Object value;
        private String type;
        private String name;
        private String ref;
        private boolean isConverted = false;
        /**
         * Create a new ValueHolder for the given value, type and name.
         * @param value the argument value
         * @param type the type of the constructor argument
         * @param name the name of the constructor argument
         */
        public ValueHolder(String value, String type, String name) {
            this.value = value;
            this.type = type;
            this.name = name;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRef() {
            return ref;
        }

        public void setRef(String ref) {
            this.ref = ref;
        }

        public boolean isConverted() {
            return isConverted;
        }

        public void setConverted(boolean converted) {
            isConverted = converted;
        }
    }

    public int getNrOfArguments(){
        return this.indexedArgumentValues.size();
    }

    public Map<Integer, ValueHolder> getIndexedArgumentValues() {
        return Collections.unmodifiableMap(indexedArgumentValues);
    }

    public void addArgumentValue(Integer index, ValueHolder valueHolder){
        indexedArgumentValues.put(index, valueHolder);
    }



}
