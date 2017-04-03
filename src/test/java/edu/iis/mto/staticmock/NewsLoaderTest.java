package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.WebServiceNewsReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by Piotrek on 03.04.2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})
public class NewsLoaderTest {

    private WebServiceNewsReader webServiceNewsReader;

    @Before
    public void setUp(){
        Configuration configuration = Mockito.mock(Configuration.class);
        Mockito.when(configuration.getReaderType()).thenReturn("WS");

        mockStatic(ConfigurationLoader.class);
        ConfigurationLoader configurationLoader = Mockito.mock(ConfigurationLoader.class);
        Mockito.when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
        Mockito.when(configurationLoader.loadConfiguration()).thenReturn(configuration);

        webServiceNewsReader = Mockito.mock(WebServiceNewsReader.class);
        mockStatic(NewsReaderFactory.class);
        NewsReaderFactory newsReaderFactory = Mockito.mock(NewsReaderFactory.class);
        Mockito.when(newsReaderFactory.getReader(configuration.getReaderType())).thenReturn(webServiceNewsReader);
    }

    @Test
    public void checkIfPublicNewsAreAddedToPublishForOneNewsTest() throws Exception{
        IncomingNews incomingNews = new IncomingNews();
        incomingNews.add(new IncomingInfo("test test test", SubsciptionType.NONE));
        Mockito.when(webServiceNewsReader.read()).thenReturn(incomingNews);
        NewsLoader newsLoader = new NewsLoader();
        PublishableNews publishableNews = newsLoader.loadNews();
        List<String> result = (List<String>) Whitebox.getInternalState(publishableNews, "publicContent");
        assertThat(result.size(), is(equalTo(1)));
        assertThat(result.get(0), is(equalTo("test test test")));
    }
}