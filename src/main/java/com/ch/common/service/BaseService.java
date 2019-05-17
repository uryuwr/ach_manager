package com.ch.common.service;

import com.ch.common.config.MyException;
import com.ch.common.entity.BaseDomain;
import com.ch.common.entity.Items;
import com.ch.common.entity.PageCondition;
import com.ch.common.entity.Select;
import com.ch.common.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.support.ExampleMatcherAccessor;

import javax.transaction.Transactional;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Transactional
public class BaseService<T extends BaseDomain<I>, I extends Serializable> {
    @Autowired
    private BaseRepository<T, I> baseRepository;

    public boolean exists(I id) {

        return findOne(id) != null;
    }

    public T findOne(I id) {

        checkId(id);
        return baseRepository.getOne(id);
    }

    protected void checkId(I id) {

        if (id == null) {
            throw new MyException("400", "id为空");
        }
    }

    public T findStrictOne(I id) {

        T t = findOne(id);

        if (t == null) {
            throw new MyException("400", "无此记录");
        }

        return t;
    }

    public List<T> findAll() {
        return baseRepository.findAll();
    }

    public T add(T t) {
        if (null != t.getId() && exists(t.getId())) {
            throw new MyException("400", "该数据已存在");
        }
        t.setCreateTime(new Date());
        return baseRepository.save(t);
    }

    protected T update(T t) {
        checkId(t.getId());
        if (!exists(t.getId())) {
            throw new MyException("400", "该记录不存在");
        }
        return baseRepository.save(t);
    }

    public T update(Map<String, Object> map, T old) {
        Iterator<Map.Entry<String, Object>> iter = map.entrySet().iterator();
        try {
            //遍历map
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                String setkey = "set" + entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1);
                Object setvalue = entry.getValue();
                Class<?> settype = old.getClass().getDeclaredField(entry.getKey()).getType();
                old.getClass().getMethod(setkey.trim(), settype).invoke(old, setvalue);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return this.update(old);
    }

    public T delete(I id) {
        T t = findStrictOne(id);
        delete(t);
        return t;
    }

    protected void delete(T t) {
        baseRepository.delete(t);
    }

    public Items<T> list(PageCondition page, T t) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        t.setDeleted(false);
        Select select = page.getSelect();

        Integer offset = page.getOffset();
        Integer limit = page.getLimit();
        Boolean count = page.getCount();
        if (count != null && count && limit != null) {
            //排序规则
            Sort sort = new Sort(Sort.Direction.DESC, "createTime");
            PageRequest pageRequest = PageRequest.of(offset == null ? 0 : offset, limit, sort);

            ExampleMatcher matcher = setMatcher(select);

            if (select != null && select.getField() != null && select.getValue() != null) {
                PropertyDescriptor pd = new PropertyDescriptor(select.getField(), t.getClass());
                Method method = pd.getWriteMethod();
                method.invoke(t, select.getValue());
            }

            Example<T> example = Example.of(t, matcher);
            Page<T> pageUser = baseRepository.findAll(example, pageRequest);
            List<T> ret = pageUser.getContent();
            return Items.of(ret, pageUser.getTotalElements());
        } else {
            Example<T> example = Example.of(t);
            Sort sort = new Sort(Sort.Direction.DESC, "createTime");
            List<T> ret = baseRepository.findAll(example, sort);
            return Items.of(ret, ret.size());
        }
    }

    private ExampleMatcher setMatcher(Select select) {
        if (select != null && select.getOperation() != null) {
            switch (select.getOperation()) {
                case LIKE:
                    return ExampleMatcher.matching()
                            .withMatcher("deleted", ExampleMatcher.GenericPropertyMatchers.exact())
                            .withMatcher(select.getField(), ExampleMatcher.GenericPropertyMatchers.contains());
                case EQ:
                    return  ExampleMatcher.matching()
                            .withMatcher("deleted", ExampleMatcher.GenericPropertyMatchers.exact())
                            .withMatcher(select.getField(), ExampleMatcher.GenericPropertyMatchers.contains());
                case START:
                    return  ExampleMatcher.matching()
                            .withMatcher("deleted", ExampleMatcher.GenericPropertyMatchers.exact())
                            .withMatcher(select.getField(), ExampleMatcher.GenericPropertyMatchers.contains());
                case END:
                    return  ExampleMatcher.matching()
                            .withMatcher("deleted", ExampleMatcher.GenericPropertyMatchers.exact())
                            .withMatcher(select.getField(), ExampleMatcher.GenericPropertyMatchers.contains());
            }
        }
        return ExampleMatcher.matching()
                .withMatcher("deleted", ExampleMatcher.GenericPropertyMatchers.exact());
    }

    public Items<T> list(PageCondition page) {
        Integer offset = page.getOffset();
        Integer limit = page.getLimit();
        Boolean count = page.getCount();
        if (count != null && count && limit != null) {
            PageRequest pageRequest = new PageRequest(offset == null ? 0 : offset, limit);
            Page<T> pageUser = baseRepository.findAll(pageRequest);
            List<T> ret = pageUser.getContent();
            return Items.of(ret, pageUser.getTotalElements());
        } else {
            Sort sort = new Sort(Sort.Direction.DESC, "createTime");
            List<T> ret = baseRepository.findAll(sort);
            return Items.of(ret, ret.size());
        }
    }
}
