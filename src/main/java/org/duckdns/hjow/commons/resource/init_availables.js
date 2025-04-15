/**
 * 사용 가능한 메소드 목록을 반환합니다.
 */
function availables() {
    var listObj = list();
    
    listObj.add("alert");
    listObj.add("confirm");
    listObj.add("askInput");
    listObj.add("askFileOpen");
    listObj.add("askFileSave");
    
    listObj.add("String");
    listObj.add("Date");
    listObj.add("convertDateFromTimeMills");
    listObj.add("today");
    listObj.add("parseInt");
    listObj.add("parseFloat");
    listObj.add("list");
    listObj.add("Array");
    listObj.add("map");
    listObj.add("Object");
    listObj.add("isEmpty");
    listObj.add("isInteger");
    listObj.add("parseBoolean");
    listObj.add("subtractDate");
    listObj.add("isNull");
    listObj.add("getClassType");
    listObj.add("isEquals");
    listObj.add("splitString");
    listObj.add("createEmptyByteArray");
    listObj.add("emptyBytes");
    listObj.add("newJsonObject");
    listObj.add("newJsonArray");
    listObj.add("parseJson");
    listObj.add("translate");
    listObj.add("getAppParamKeys");
    listObj.add("getAppParam");
    
    listObj.add("sleep");
    listObj.add("tryWithoutReturn");
    listObj.add("subEngine");
    listObj.add("newThread");
    listObj.add("runtime_gc");
    listObj.add("runtime_maxMemory");
    listObj.add("runtime_freeMemory");
    listObj.add("runtime_totalMemory");
    
    listObj.add("ui_createsByJson");
    listObj.add("ui_create");
    listObj.add("ui_scroll");
    
    listObj.add("hash");
    listObj.add("hashBytes");
    listObj.add("hexString");
    listObj.add("hexBytes");
    listObj.add("encrypt");
    listObj.add("decrypt");
    
    listObj.add("file_exists");
    listObj.add("file_isDirectory");
    listObj.add("file_listIn");
    listObj.add("file_makeDirectory");
    listObj.add("file_delete");
    
    listObj.add("file_readBytes");
    listObj.add("file_writeBytes");
    listObj.add("file_readString");
    listObj.add("file_writeString");
    listObj.add("file_readProp");
    listObj.add("file_writeProp");
    
    listObj.add("net_sendPost");
    listObj.add("net_sendPostBytes");
    // listObj.add("net_socket_open");
    // listObj.add("net_socket_connect");
    
    listObj.add("web_create");
    listObj.add("web_server");
    
    listObj.add("jdbc_connect");
    listObj.add("jdbc_closeAllConnections");

    return listObj;
};