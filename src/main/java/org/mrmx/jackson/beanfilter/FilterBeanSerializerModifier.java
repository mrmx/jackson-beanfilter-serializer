package org.mrmx.jackson.beanfilter;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import java.util.List;

/**
 * FilterBeanSerializerModifier
 *
 * @author mrmx
 */
public class FilterBeanSerializerModifier extends BeanSerializerModifier {

    private final ImmutableMultimap<Class, String> filters;

    static FilterBeanSerializerModifier excluding(ImmutableMultimap<Class, String> filters) {
        return new FilterBeanSerializerModifier(filters);
    }

    private FilterBeanSerializerModifier(ImmutableMultimap<Class, String> filters) {
        this.filters = filters;
    }

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
            BeanDescription beanDesc,
            List<BeanPropertyWriter> beanProperties) {
        if (filters == null || filters.size() == 0) {
            return beanProperties;
        }

        ImmutableCollection<String> filter = filters.get(beanDesc.getBeanClass());
        if (filter == null) {
            return beanProperties;
        }

        List<BeanPropertyWriter> included = Lists.newArrayList();
        for (BeanPropertyWriter property : beanProperties) {
            if (!filter.contains(property.getName())) {
                included.add(property);
            }
        }

        return included;
    }
}
