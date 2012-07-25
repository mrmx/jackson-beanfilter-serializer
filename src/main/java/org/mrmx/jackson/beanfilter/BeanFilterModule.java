package org.mrmx.jackson.beanfilter;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.google.common.collect.ImmutableMultimap;


/**
 *
 * @author mrmx
 */
public class BeanFilterModule extends Module {
    private static final Version MODULE_VERSION = new Version(0, 1, 0, null,null,null);
    private final ImmutableMultimap<Class, String> filters;
    
     public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {        
        private final ImmutableMultimap.Builder<Class, String> filterBuilder = new ImmutableMultimap.Builder<Class, String>();

        private Builder() {            
        }

        public Builder exclude(Class beanClass, String name) {
            filterBuilder.put(beanClass, name);
            return this;
        }

        public BeanFilterModule build() {
            return new BeanFilterModule(this);
        }
    }

    private BeanFilterModule(Builder builder) {    
        filters = builder.filterBuilder.build();
    }
    
    @Override
    public String getModuleName() {
        return getClass().getSimpleName();
    }

    @Override
    public Version version() {
        return MODULE_VERSION;
    }

    @Override
    public void setupModule(SetupContext context) {
        context.addBeanSerializerModifier(FilterBeanSerializerModifier.excluding(filters));
    }
    
}