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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.duckdns.hjow.commons.util.ClassUtil;

/**
 * 여러 유형의 Writer 스트림을 통해 데이터를 가공해야 할 경우 사용합니다.
 * 
 * @author HJOW
 *
 */
public class ChainWriter extends Writer implements ChainObject
{
    private static final long serialVersionUID = 5514133029267791071L;
    protected boolean locked = false;
    private List<Writer> chains = new Vector<Writer>();
    
    public ChainWriter(Writer firstStream)
    {
        chains.add(firstStream);
    }
    
    public void put(Class<? extends Writer> outputStreamClass) throws Exception
    {
        if(locked) throw new IOException("Cannot put the input stream on the chain stream which is already locked.");
        Writer stream = outputStreamClass.getConstructor(Writer.class).newInstance(getWriter(false));
        chains.add(stream);
    }
    public Writer getWriter()
    {
        return getWriter(true);
    }
    private Writer getWriter(boolean lock)
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

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        getWriter().write(cbuf, off, len);
    }

    @Override
    public void flush() throws IOException {
        getWriter().flush();
    }

    @Override
    public List<String> getElementTypes() {
        List<String> elements = new ArrayList<String>();
        for(Writer w : chains) {
            elements.add(w.getClass().getName());
        }
        
        return elements;
    }

    @Override
    public void releaseResource() {
        try { close(); } catch(IOException e) { throw new RuntimeException(e); }
    }
}
