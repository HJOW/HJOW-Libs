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
package hjow.common.data;

import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class MutableNumber extends Number {
    private static final long serialVersionUID = 513079363055877118L;

    @Override
    public int intValue() {
        return getNumber().intValue();
    }

    @Override
    public long longValue() {
        return getNumber().longValue();
    }

    @Override
    public float floatValue() {
        return getNumber().floatValue();
    }

    @Override
    public double doubleValue() {
        return getNumber().doubleValue();
    }
    
    /** Mutable 하지 않은 원 Number 객체를 반환합니다. */
    public abstract Number getNumber();
    /** 값을 변경합니다. */
    public abstract void setNumber(Number value);
    /** 값을 더합니다. */
    public abstract void add(Number numbers);
    /** 값을 뺍니다. */
    public abstract void subtract(Number numbers);
    /** 값을 더합니다. */
    public abstract void multiply(Number numbers);
    /** 값을 나눕니다. */
    public abstract void divide(Number numbers);
    /** 소수 부분을 가지는지 여부를 반환합니다. */
    public abstract boolean hasFloatingValue();
    
    /** 값에 정수 1을 더합니다. */
    public void increase() {
        Number num = getNumber();
        if(num instanceof BigInteger) {
            num = ((BigInteger) num).add(BigInteger.ONE);
        } else if(num instanceof BigDecimal) {
            num = ((BigDecimal) num).add(BigDecimal.ONE);
        } else if(num instanceof Integer) {
            num = new Integer(num.intValue() + 1);
        } else if(num instanceof Long) {
            num = new Long(num.longValue() + 1);
        } else if(num instanceof Float) {
            num = new Float(num.floatValue() + 1);
        } else if(num instanceof Double) {
            num = new Double(num.doubleValue() + 1);
        } else if(num instanceof MutableInteger) {
            num = new Long(((MutableInteger) num).getValue() + 1);
            ((MutableInteger) num).setValue(num.longValue());
        } else if(num instanceof MutableFloat) {
            num = new Double(((MutableFloat) num).getValue() + 1);
            ((MutableFloat) num).setValue(num.doubleValue());
        }
        setNumber(num);
    }
}
