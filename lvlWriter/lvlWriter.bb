
;
;lvl file utils to be included
;

Dim lvl_stack(100)
Global lvl_file,lvl_tos

Function lvlSetFile( file )
	lvl_tos=0
	lvl_file=file
End Function

;***** functions for reading from lvl files *****

Function lvlReadByte()
	Return ReadByte( lvl_file )
End Function

Function lvlReadInt()
	Return ReadInt( lvl_file )
End Function

Function lvlReadFloat#()
	Return ReadFloat( lvl_file )
End Function

Function lvlReadString$()
	Repeat
		ch=lvlReadByte()
		If ch=0 Return t$
		t$=t$+Chr$(ch)
	Forever
End Function

Function lvlReadChunk$()
	For k=1 To 4
		tag$=tag$+Chr$(lvlReadByte())
	Next
	sz=ReadInt( lvl_file )
	lvl_tos=lvl_tos+1
	lvl_stack(lvl_tos)=FilePos( lvl_file )+sz
	Return tag$
End Function

Function lvlSearchChunk$(chnk$)
	times=0
	For k=1 To 16
		tag$=tag$+Chr$(lvlReadByte())
	Next
	pos=Instr(tag$,chnk$,1)
	If pos=0 pos=1
	re$=Mid$(tag$,pos,4)
	If chnk$=re$
		SeekFile lvl_file,FilePos(lvl_file)-17+pos
		Return True
	EndIf
	If Eof (lvl_file) Return False
End Function 

Function lvlExitChunk()
	SeekFile lvl_file,lvl_stack(lvl_tos)
	lvl_tos=lvl_tos-1
End Function

Function lvlChunkSize()
	Return lvl_stack(lvl_tos)-FilePos( lvl_file )
End Function

;***** Functions for writing to lvl files *****

Function lvlWriteByte( n )
	WriteByte( lvl_file,n )
End Function

Function lvlWriteInt( n )
	WriteInt( lvl_file,n )
End Function

Function lvlWriteFloat( n# )
	WriteFloat( lvl_file,n )
End Function

Function lvlWriteString( t$ )
	For k=1 To Len( t$ )
		ch=Asc(Mid$(t$,k,1))
		lvlWriteByte(ch)
		If ch=0 Return
	Next
	lvlWriteByte( 0 )
End Function

Function lvlBeginChunk( tag$ )
	lvl_tos=lvl_tos+1
	For k=1 To 4
		lvlWriteByte(Asc(Mid$( tag$,k,1 )))
	Next
	lvlWriteInt( 0 )
	lvl_stack(lvl_tos)=FilePos( lvl_file )
End Function

Function lvlEndChunk()
	n=FilePos( lvl_file )
	SeekFile lvl_file,lvl_stack(lvl_tos)-4
	lvlWriteInt( n-lvl_stack(lvl_tos) )
	SeekFile lvl_file,n
	lvl_tos=lvl_tos-1
End Function