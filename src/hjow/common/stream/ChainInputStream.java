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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

/**
 * 여러 유형의 InputStream 스트림을 통해 데이터를 가공해야 할 경우 사용합니다.
 * 
 * @author HJOW
 *
 */
public class ChainInputStream extends InputStream implements ChainObject
{
	private static final long serialVersionUID = 3326788924330343979L;
	protected boolean locked = false;
    protected List<InputStream> chains = new Vector<InputStream>();    
    
    public ChainInputStream(InputStream firstStream)
    {
        chains.add(firstStream);
    }
    
    public void put(Class<? extends InputStream> inputStreamClass) throws Exception
    {
        if(locked) throw new IOException("Cannot put the input stream on the chain stream which is already locked.");
        InputStream stream = inputStreamClass.getConstructor(InputStream.class).newInstance(getInputStream(false));
        chains.add(stream);
    }
    public InputStream getInputStream()
    {
        return getInputStream(true);
    }
    private InputStream getInputStream(boolean lock)
    {
        locked = lock;
        return chains.get(chains.size() - 1);
    }
    
    @Override
    public void close() throws IOException
    {
        for(int i=chains.size()-1; i>=0; i--)
        {
            try
            {
                chains.get(i).close();
            }
            catch(Throwable e)
            {
                
            }
        }
        locked = true;
    }

    public void put(String streamName) throws Exception
    {
        if(streamName.equals("Data"))
        {
            put(DataInputStream.class);
        }
        else if(streamName.equals("Zip"))
        {
            put(ZipInputStream.class);
        }
        else if(streamName.equals("GZip"))
        {
            put(GZIPInputStream.class);
        }
    }

	@Override
	public int read() throws IOException {
		return getInputStream().read();
	}
	
	@Override
	public int available() throws IOException {
		if(chains.isEmpty()) return 0;
        return getInputStream().available();
    }
	
	@Override
	public synchronized void reset() throws IOException {
        getInputStream().reset();
    }
	
	@Override
	public List<String> getElementTypes() {
		List<String> elements = new ArrayList<String>();
		for(InputStream r : chains) {
			elements.add(r.getClass().getName());
		}
		
		return elements;
	}
	
	@Override
	public void releaseResource() {
		try { close(); } catch(IOException e) { throw new RuntimeException(e); }
	}
}
