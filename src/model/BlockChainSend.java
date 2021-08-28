package model;

import json.JsonSend;
import util.P2PNet;

public class BlockChainSend {
	
	private JsonSend jsonSend;
	
	public void broadCastingMinedBlock(Block tmpBlock) {
		jsonSend = new JsonSend(P2PNet.getServerListener());
		jsonSend.sendBlockMinedMessage(tmpBlock);
	}
	
	public void broadCastingVerifiedResult(Block tmpBlock) {
		jsonSend = new JsonSend(P2PNet.getServerListener());
		jsonSend.sendVerifiedResultMessage(tmpBlock.isValid());
	}
}
