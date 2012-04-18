package dfh.json.simple.test;

import static org.junit.Assert.*;

import org.junit.Test;

import dfh.grammar.Matcher;
import dfh.grammar.Options;
import dfh.json.simple.Converter;

public class Regressions {

	@Test
	public void test() {
		String s = "{\"opts\":{\"return_text\":false,\"clean\":false},\"msgs\":[[{\"text\":\"There seems to be a weird text under the login box after you click the email icon from the homepage.\\n\\nI just had Norton clear my computer of all viruses that somehow got downloaded from videos. I sent\\nComcast an email about the issue. The text looks like this:\\n\\n&&2\\u001c)&& #&G&Ax\\u001d&ʽ&&*o&X&H&&&\\u001b\\u0004&&s&&!w&M\\u0010&>CN &\\u0012\\b&&\\\\&&&&&&&&&\\u0006d&\\u0007-&g&\\u0003&&&&>&& &x9&_\\u0011&H1#;^\\n\\u0006&n<\\u0017&&Ze&.\\u0017&&T\\u0019&&Ę&&&\\u001d&Yj&&&&[&&&+]&&z&4&&N& V&M߶>kŁ&*&&ޭ\\u0019&ʮ :& &%\\u0013&ū&&~3g\\u0003&n&&\\u0010R\\n&<\\u0010J&&G1&pw&&&f&f&(&\\u0018&\\u0005&d\\\\&&T$&&&Zޖ\\u0012a\\u001c#&\\u001b\\\"&l&&V&9\\u0014&&&&\\b\\u0005&{ Ԁ\\u0016&#&&\\u0002\\u0017&9^rY*Y]K&\\u0007e:&&& &9&\\u0013&\\u0006\\u0018&ҁ&&\\u000f\\u0016a\\nN&N\\u001a& V\\u001bݘҗ&s&&&\\u0014&&P&&z&;&zn$}rD&&&z˰&8M/&&N@i&&&\\u000e&&&m&&&&&&&M&&\\u0013&&\\u0005x&d\\n&&&g&&&!&\\\"&ҍg&&h3m!K&&&/,&.d@&H匍\\\\k&&&\\u001c!&&\\u0019h\\u0007q&&&`\\u0011\\\\\\u0013\\u0019&A'&&&&ZH\\u000f&&wӷ\\u000f1&&&a߶'#&0'\\u001f\\u0012U'\\u0016&\\u0018&&&&&Ö9&?y&&)Z&&>&'&\\n\\u000e&&L&2&q\\u001e&+&\",\"is_cs\":false,\"id\":\"30482\"}]]}";
		Matcher m = Converter.g.matches(s, new Options().study(false));
		assertNotNull(m.match());
	}

}
