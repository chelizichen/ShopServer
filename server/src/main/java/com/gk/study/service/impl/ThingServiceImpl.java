package com.gk.study.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.study.entity.Classification;
import com.gk.study.entity.Thing;
import com.gk.study.entity.ThingTag;
import com.gk.study.mapper.ClassificationMapper;
import com.gk.study.mapper.ThingMapper;
import com.gk.study.mapper.ThingTagMapper;
import com.gk.study.service.ThingService;
import com.gk.study.sgrid.xt.CollaborativeFactor;
import com.gk.study.sgrid.xt.CollaborativeFilterImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ThingServiceImpl extends ServiceImpl<ThingMapper, Thing> implements ThingService {
    public CollaborativeFilterImpl collaborativeFilter;

    public void InitCollaborativeFilterImpl() {
        CollaborativeFilterImpl collaborativeFilter = new CollaborativeFilterImpl();
        collaborativeFilter.WithDefaultLength(7);
        collaborativeFilter.WithLimitMaxFactoryWeight(70.0);
        ArrayList<CollaborativeFactor> collaborativeFactories = new ArrayList<>();
        QueryWrapper<Classification> queryWrapper = new QueryWrapper<>();
        List<Classification> classifications = classificationMapper.selectList(queryWrapper);
        for (Classification classification : classifications) {
            collaborativeFactories.add(new CollaborativeFactor(0.0, Math.toIntExact(classification.getId()), classification.getTitle()));
        }
        collaborativeFilter.WithDefaultFactories(collaborativeFactories);
        this.collaborativeFilter = collaborativeFilter;
    }

    @Autowired
    ThingMapper mapper;

    @Autowired
    ThingTagMapper thingTagMapper;

    @Autowired
    ClassificationMapper classificationMapper;

    @Override
    public List<Thing> getThingList(String keyword, String sort, String c, String tag) {
        if (this.collaborativeFilter == null) {
            this.InitCollaborativeFilterImpl();
        }
        if (sort != null && (sort.equals("hot") || sort.equals("recommend"))) {
            List<Thing> things = new ArrayList<>();
            List<CollaborativeFactor> currentFactories = this.collaborativeFilter.getCurrentFactories();
            for (CollaborativeFactor currentFactory : currentFactories) {
                QueryWrapper<Thing> queryWrapper = new QueryWrapper<>();
                int size = (int) ((currentFactory.getWeight() + 5.0) / 10);
                System.out.println("size" + size);
                Page<Thing> page = new Page<>(1, size);
                System.out.println("curr|" + currentFactory);
                queryWrapper.eq("classification_id", currentFactory.getType());
                queryWrapper.orderBy(true, false, "create_time");
                IPage<Thing> thingPage = mapper.selectPage(page, queryWrapper);
                things.addAll(thingPage.getRecords());
                System.out.println("page-size ::" + thingPage.getRecords().size());
            }
            for (Thing thing : things) {
                System.out.println("thing" + thing.title);
            }
            return things;
        }

        QueryWrapper<Thing> queryWrapper = new QueryWrapper<>();

        // 搜索
        queryWrapper.like(StringUtils.isNotBlank(keyword), "title", keyword);

        // 排序
        if (StringUtils.isNotBlank(sort)) {
            if (sort.equals("recent")) {
                queryWrapper.orderBy(true, false, "create_time");
            } else if (sort.equals("hot") || sort.equals("recommend")) {
                queryWrapper.orderBy(true, false, "pv");
            }
        } else {
            queryWrapper.orderBy(true, false, "create_time");
        }

        // 根据分类筛选
        if (StringUtils.isNotBlank(c) && !c.equals("-1")) {
            queryWrapper.eq(true, "classification_id", c);
        }

        List<Thing> things = mapper.selectList(queryWrapper);

        // tag筛选
        if (StringUtils.isNotBlank(tag)) {
            List<Thing> tThings = new ArrayList<>();
            QueryWrapper<ThingTag> thingTagQueryWrapper = new QueryWrapper<>();
            thingTagQueryWrapper.eq("tag_id", tag);
            List<ThingTag> thingTagList = thingTagMapper.selectList(thingTagQueryWrapper);
            for (Thing thing : things) {
                for (ThingTag thingTag : thingTagList) {
                    if (thing.getId().equals(thingTag.getThingId())) {
                        tThings.add(thing);
                    }
                }
            }
            things.clear();
            things.addAll(tThings);
        }

        // 附加tag
        for (Thing thing : things) {
            QueryWrapper<ThingTag> thingTagQueryWrapper = new QueryWrapper<>();
            thingTagQueryWrapper.lambda().eq(ThingTag::getThingId, thing.getId());
            List<ThingTag> thingTags = thingTagMapper.selectList(thingTagQueryWrapper);
            List<Long> tags = thingTags.stream().map(ThingTag::getTagId).collect(Collectors.toList());
            thing.setTags(tags);
        }
        return things;
    }

    @Override
    public void createThing(Thing thing) {
        System.out.println(thing);
        thing.setCreateTime(String.valueOf(System.currentTimeMillis()));

        if (thing.getPv() == null) {
            thing.setPv("0");
        }
        if (thing.getScore() == null) {
            thing.setScore("0");
        }
        if (thing.getWishCount() == null) {
            thing.setWishCount("0");
        }
        mapper.insert(thing);
        // 更新tag
        setThingTags(thing);
    }

    @Override
    public void deleteThing(String id) {
        mapper.deleteById(id);
    }

    @Override
    public void updateThing(Thing thing) {

        // 更新tag
        setThingTags(thing);

        mapper.updateById(thing);
    }

    @Override
    public Thing getThingById(String id) {
        Thing thing = mapper.selectById(id);
        Long classificationId = thing.getClassificationId();
        if (this.collaborativeFilter == null) {
            this.InitCollaborativeFilterImpl();
        }
        this.collaborativeFilter.addFactoryWeight(new CollaborativeFactor(0.0, Math.toIntExact(classificationId), ""));
        return thing;
    }

    // 心愿数加1
    @Override
    public void addWishCount(String thingId) {
        Thing thing = mapper.selectById(thingId);
        thing.setWishCount(String.valueOf(Integer.parseInt(thing.getWishCount()) + 1));
        mapper.updateById(thing);
    }

    // 收藏数加1
    @Override
    public void addCollectCount(String thingId) {
        Thing thing = mapper.selectById(thingId);
        thing.setCollectCount(String.valueOf(Integer.parseInt(thing.getCollectCount()) + 1));
        mapper.updateById(thing);
    }

    public void setThingTags(Thing thing) {
        // 删除tag
        Map<String, Object> map = new HashMap<>();
        map.put("thing_id", thing.getId());
        thingTagMapper.deleteByMap(map);
        // 新增tag
        if (thing.getTags() != null) {
            for (Long tag : thing.getTags()) {
                ThingTag thingTag = new ThingTag();
                thingTag.setThingId(thing.getId());
                thingTag.setTagId(tag);
                thingTagMapper.insert(thingTag);
            }
        }
    }

}
