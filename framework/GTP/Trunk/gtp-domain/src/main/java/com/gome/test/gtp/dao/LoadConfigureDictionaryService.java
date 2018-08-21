package com.gome.test.gtp.dao;

import com.gome.test.gtp.model.ConfigureDictionary;
import com.gome.test.utils.Logger;
import com.gome.test.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by lizonglin on 2015/4/27.
 */
@Service
public class LoadConfigureDictionaryService {
    @Autowired
    private ConfigureDictionaryDao cdDao;

    private volatile static List<ConfigureDictionary> cdList;

    private volatile static long lastupdateTime = System.currentTimeMillis();

    public synchronized void refresh(boolean forceRefresh) {
        if (System.currentTimeMillis() - lastupdateTime > 1000 * 60 || forceRefresh) {
//            System.out.println("refresh Dictionary");
            Logger.info("refresh Dictionary");
            cdList = cdDao.getAll();

            lastupdateTime = System.currentTimeMillis();
        }
    }

    @PostConstruct
    private void init() {
        refresh(true);
    }

    /**
     * load typeMaps(parentItem)
     */
    private HashMap<Integer, String> loadParentIdNameMap() {
        /**
         * idNameTypeMap: <parentItem.ID,parentItem.ItemValue>
         */
        refresh(false);
        HashMap<Integer, String> idNameTypeMap = new HashMap<Integer, String>();
        if (cdList == null) {
            refresh(true);
        }
        for (int i = 0; i < cdList.size(); i++) {
            ConfigureDictionary cd = cdList.get(i);
            if (cd.getItemParentID() == 0) {
                idNameTypeMap.put(cd.getId(), cd.getItemName());
            }
        }
        return idNameTypeMap;
    }

    private List<Integer> loadParentIdList() {
        /**
         * idNameTypeMap to pidList(All parentItem's id)
         */
        Collection<Integer> pidSet = loadParentIdNameMap().keySet();
        return new ArrayList<Integer>(pidSet);
    }

    public List getValueNameList(String type) {
        HashMap<String, HashMap<String, Integer>> ssiMap = loadSSIMap();
        HashMap<String, Integer> nameValueMap = ssiMap.get(type);
        Collection<String> nameSet = nameValueMap.keySet();
        List<String> itemList = new ArrayList<String>(nameSet);
        Collections.sort(itemList);
        List resultList = new ArrayList<Object>();
        for (int i = 0; i < itemList.size(); i++) {
            List valueNameList = new ArrayList<Object>();
            valueNameList.add(itemList.get(i));
            valueNameList.add(nameValueMap.get(itemList.get(i)));
            resultList.add(valueNameList.toArray());
        }
        return resultList;
    }

    /**
     * SSI Map-----------------------------------------------------------------------------
     *
     * @return
     */

    public HashMap<String, HashMap<String, Integer>> loadSSIMap() {
        List<Integer> pidList = loadParentIdList();
        /**
         * ssiMap: <parentItem.ItemName,<childItem.ItemName,childItem.ItemValue>>
         * isiMap: <parentItem.ID,<childItem.ItemName,childItem.ItemValue>>
         */
        HashMap<String, HashMap<String, Integer>> ssiMap = new HashMap<String, HashMap<String, Integer>>();
        HashMap<Integer, HashMap<String, Integer>> isiMap = new HashMap<Integer, HashMap<String, Integer>>();
        /**
         * load isiMap
         */
        refresh(false);
        for (int i = 0; i < pidList.size(); i++) {
            HashMap<String, Integer> NVMap = new HashMap<String, Integer>();//item name-value map
            for (int j = 0; j < cdList.size(); j++) {
                ConfigureDictionary cd = cdList.get(j);
                if (cd.getItemParentID() == pidList.get(i)) {
                    NVMap.put(cd.getItemName(), cd.getItemValue());
                }
            }
            if (!NVMap.isEmpty()) {
                isiMap.put(pidList.get(i), NVMap);
            }
        }

        /**
         * transfer isiMap to ssiMap
         */
        for (int i = 0; i < pidList.size(); i++) {
            HashMap<String, Integer> tmpMap = isiMap.get(pidList.get(i));
            String parentType = loadParentIdNameMap().get(pidList.get(i));
            ssiMap.put(parentType, tmpMap);
        }
        return ssiMap;
    }

    public Integer getValueByName(String type, String name) {
        return loadSSIMap().get(type).get(name);
    }

    /**
     * get all the childItems of some type
     *
     * @param type
     * @return
     */
    public List<String> getTypeList(String type) {
        HashMap<String, HashMap<String, Integer>> ssiMap = loadSSIMap();
        HashMap<String, Integer> nameValueMap = ssiMap.get(type);
        Collection<String> nameSet = nameValueMap.keySet();
//        List<String> nameList = new ArrayList<String>(nameSet);
        return new ArrayList<String>(nameSet);
    }

    /**
     * SIS Map-------------------------------------------------------------------------------
     * <parentName, <childValue,childName>>
     *
     * @return
     */

    public HashMap<String, HashMap<Integer, String>> loadSISMap() {
        List<Integer> pidList = loadParentIdList();
        HashMap<String, HashMap<Integer, String>> sisMap = new HashMap<String, HashMap<Integer, String>>();
        HashMap<Integer, HashMap<Integer, String>> iisMap = new HashMap<Integer, HashMap<Integer, String>>();
        /**
         * load isiMap
         */
        for (int i = 0; i < pidList.size(); i++) {
            HashMap<Integer, String> VNMap = new HashMap<Integer, String>();//item value-name map
            for (int j = 0; j < cdList.size(); j++) {
                ConfigureDictionary cd = cdList.get(j);
                if (cd.getItemParentID() == pidList.get(i)) {
                    VNMap.put(cd.getItemValue(), cd.getItemName());
                }
            }
            if (!VNMap.isEmpty()) {
                iisMap.put(pidList.get(i), VNMap);
            }
        }
        /**
         * transfer iisMap to sisMap
         */
        for (int i = 0; i < pidList.size(); i++) {
            HashMap<Integer, String> tmpMap = iisMap.get(pidList.get(i));
            String parentType = loadParentIdNameMap().get(pidList.get(i));
            sisMap.put(parentType, tmpMap);
        }
        return sisMap;
    }


    public String getNameByValue(String type, Integer value) {
        HashMap<String, HashMap<Integer, String>> sis = loadSISMap();
        if (sis.containsKey(type) && sis.get(type).containsKey(value)) {
            return sis.get(type).get(value);
        }
        return value.toString();
    }

    public List<Integer> getValueList (String type) {
        HashMap<String, HashMap<Integer, String>> sisMap = loadSISMap();
        HashMap<Integer, String> isMap = sisMap.get(type);
        Collection<Integer> valueSet = isMap.keySet();
        return new ArrayList<Integer>(valueSet);
    }



    /**
     * @param list
     * @param replaceRule key:数组元素中Object[]下标  value:parentName
     * Object[]中下标为replaceRule.key的值变成类型为replaceRule.get(key)的对应值
     *
     * 例： Object[] 为：[1055,70] replaceRule为：<1,taskStatus>变换后 Object[1055,Completed]
     */
    public void replaceValue(List<Object[]> list, Map<Integer, String> replaceRule) {
        if (list != null) {
            for (Object[] obj : list) {
                for (Map.Entry<Integer, String> rule : replaceRule.entrySet()) {
                    if (obj.length > rule.getKey()) {

                        if(obj[rule.getKey()]!=null && !StringUtils.isEmpty(obj[rule.getKey()].toString())) {
                            //                    System.out.println(String.format("%s %s", rule.getValue(), obj[rule.getKey()]));
                            obj[rule.getKey()] = getNameByValue(rule.getValue(), Integer.parseInt(obj[rule.getKey()].toString()));
                        }

                    }
                }
            }
        }
    }
//    private HashMap<String, Integer> loadParentNameIdMap () {
//        /**
//         * nameIdTypeMap: <parentItem.ItemValue,parentItem.ID>
//         */
//        HashMap<String, Integer> nameIdTypeMap = new HashMap<String, Integer>();
//        for (int i = 0; i < cdList.size(); i++) {
//            ConfigureDictionary cd = cdList.get(i);
//            if (cd.getItemParentID() == 0) {
//                nameIdTypeMap.put(cd.getItemName(),cd.getId());
//            }
//        }
//        return nameIdTypeMap;
//    }

}
