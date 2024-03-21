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

package hjow.common.stream;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import hjow.common.util.ClassUtil;

/**
 * 여러 유형의 Reader 스트림을 통해 데이터를 가공해야 할 경우 사용합니다.
 * 
 * @author HJOW
 *
 */
public class ChainReader extends Reader implements ChainObject
{
	private static final long serialVersionUID = -3615966375619099821L;
	protected boolean locked = false;
    private List<Reader> chains = new Vector<Reader>();
    
    public ChainReader(Reader firstStream)
    {
        chains.add(firstStream);
    }
    
    public void put(Class<? extends Reader> inputStreamClass) throws Exception
    {
        if(locked) throw new IOException("Cannot put the input stream on the chain stream which is already locked.");
        Reader stream = inputStreamClass.getConstructor(Reader.class).newInstance(getReader(false));
        chains.add(stream);
    }
    public Reader getReader()
    {
        return getReader(true);
    }
    private Reader getReader(boolean lock)
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
	public int read(char[] cbuf, int off, int len) throws IOException {
		return getReader().read(cbuf, off, len);
	}
	
	@Override
    public boolean ready() throws IOException {
        return getReader().ready();
    }

    @Override
    public boolean markSupported() {
        return getReader().markSupported();
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        getReader().mark(readAheadLimit);
    }

    @Override
    public void reset() throws IOException {
        getReader().reset();
    }
    
    @Override
	public List<String> getElementTypes() {
		List<String> elements = new ArrayList<String>();
		for(Reader r : chains) {
			elements.add(r.getClass().getName());
		}
		
		return elements;
	}
    
    @Override
	public void releaseResource() {
		try { close(); } catch(IOException e) { throw new RuntimeException(e); }
	}
}
