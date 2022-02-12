package lj.study.test;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * Created by lijing on 2017/7/27.
 * Desc:
 */

@ContextConfiguration(locations = {"classpath:dubbo-consumer.xml"})
public class BaseTestNG extends AbstractTestNGSpringContextTests {

}
