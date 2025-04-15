/*
 
 Copyright 2019 HJOW

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 
 */

package org.duckdns.hjow.commons.stream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipOutputStream;

import org.duckdns.hjow.commons.util.ClassUtil;

/**
 * 여러 유형의 OutputStream 스트림을 통해 데이터를 가공해야 할 경우 사용합니다.
 * 
 * @author HJOW
 *
 */
public class ChainOutputStream extends OutputStream implements ChainObject
{
    private static final long serialVersionUID = -1685900927437490377L;
    protected boolean locked = false;
    protected List<OutputStream> chains = new Vector<OutputStream>();    
    
    public ChainOutputStream(OutputStream firstStream)
    {
        chains.add(firstStream);
    }
    
    public void put(Class<? extends OutputStream> outputStreamClass) throws Exception
    {
        if(locked) throw new IOException("Cannot put the input stream on the chain stream which is already locked.");
        OutputStream stream = outputStreamClass.getConstructor(InputStream.class).newInstance(getOutputStream(false));
        chains.add(stream);
    }
    public OutputStream getOutputStream()
    {
        return getOutputStream(true);
    }
    private OutputStream getOutputStream(boolean lock)
    {
        locked = lock;
        return chains.get(chains.size() - 1);
    }
    
    @Override
    public synchronized void close() throws IOException
    {
        locked = true;
        for(int i=chains.size()-1; i>=0; i--)
        {
            ClassUtil.closeAll(chains.get(i));
        }
        chains.clear();
    }

    @SuppressWarnings("unchecked")
    public void put(String streamName) throws Exception
    {
        if(streamName.equals("Data"))
        {
            put(DataOutputStream.class);
        }
        else if(streamName.equals("Zip"))
        {
            put(ZipOutputStream.class);
        }
        else if(streamName.equals("GZip"))
        {
            put(GZIPOutputStream.class);
        }
        else
        {
            put((Class<? extends OutputStream>) Class.forName(streamName));
        }
    }

    @Override
    public void write(int b) throws IOException {
        getOutputStream().write(b);
    }
    
    @Override
    public void flush() throws IOException {
        getOutputStream().flush();
    }
    
    @Override
    public List<String> getElementTypes() {
        List<String> elements = new ArrayList<String>();
        for(OutputStream r : chains) {
            elements.add(r.getClass().getName());
        }
        
        return elements;
    }
    
    @Override
    public void releaseResource() {
        try { close(); } catch(IOException e) { throw new RuntimeException(e); }
    }
}
