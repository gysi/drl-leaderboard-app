package de.gregord.drlleaderboardbackend.entities;

import de.gregord.drlleaderboardbackend.util.TsidUtil;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class TsidGenerator implements IdentifierGenerator {
    static final String GENERATOR_NAME = "TSID";

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return TsidUtil.TSID_FACTORY.create().toLong();
    }
}
