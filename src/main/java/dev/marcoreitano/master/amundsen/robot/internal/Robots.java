package dev.marcoreitano.master.amundsen.robot.internal;

import dev.marcoreitano.master.amundsen.robot.Robot;
import dev.marcoreitano.master.amundsen.robot.RobotId;
import org.jmolecules.ddd.integration.AssociationResolver;
import org.jmolecules.ddd.types.Repository;

import java.util.Optional;

public interface Robots extends Repository<Robot, RobotId>, AssociationResolver<Robot, RobotId> {
    Robot save(Robot robot);

    Optional<Robot> findById(RobotId robotId);
}
