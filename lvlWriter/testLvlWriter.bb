Include "lvlWriter.bb"

SaveLevel()


Function SaveLevel(f_name$="default.lvl")
    file=WriteFile(f_name$)
	lvlSetFile(file)
    lvlBeginChunk("VOID") ;file header
    	lvlWriteInt(1) ;version number
		lvlWriteString(CurrentDate$()) 
		lvlWriteString(CurrentTime$())
		lvlWriteString("Test level only: No mesh data: No embedded media")		
    lvlEndChunk()


music_name$="reed-a_synthetic_device.xm"
music_file=ReadFile(music_name$)
image_name$="crete41.jpg"
image_file=ReadFile(image_name$)
   lvlBeginChunk("EMBD")
		lvlBeginChunk("MUSC")
			lvlWriteString(music_name$)
			lvlBeginChunk("DATA")
			While Not Eof(music_file)
				lvlWriteByte(ReadByte(music_file))
			Wend
			CloseFile(music_file)
			lvlEndChunk()
		lvlEndChunk()
		lvlBeginChunk("IMGS")
			lvlWriteString(image_name$)
			lvlBeginChunk("DATA")
			While Not Eof(image_file)
				lvlWriteByte(ReadByte(image_file))
			Wend
			lvlEndChunk()
		lvlEndChunk()
   lvlEndChunk()
CloseFile file
End Function