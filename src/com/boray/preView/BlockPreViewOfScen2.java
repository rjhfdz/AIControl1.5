package com.boray.preView;

public class BlockPreViewOfScen2 {
/*	void HP_Preview_Run_Scan()//PreviewTest2018_12_11A1
	{
		int tmp;
		int time_ms_cnt=0;

//		if (HP_Preview_Status==FALSE) return;
//		if (HP_This_SceneNo_Bak>24) return;	// 
//		if (HP_This_SceneNo_Bak==0) return;	//��Ϊ0��ǿ�йأ�1-16������
		//******************************************
//		Fog_Work_Logic_Loop1ms();//��������߼�����   
//		if (HP_Setup_Total_Time==0) return;
		//---------------------------
		HP_Preview_ExecutionTime_OneGroup(1);//
		NHP_Preview_ExecutionTime_OneGroup(PreviewGroup);
		//---------------------------
		time_ms_cnt++;
//		if (time_ms_cnt>=1000)//100ms  //
		if(NotHP_TimeOut_1S_Flag==1)
		{
			NotHP_TimeOut_1S_Flag=0;
	//printf("\r\n �ֶ�HP����=:%d",(int)(1));//
			time_ms_cnt=0;
	   		HP_This_Run_Time_Cnt++;//�ֶ��������ʱ������ۼ�
			//---------------------
			//---------------------
			//MODI2017_03_20A6
			//���ʱ��Ƭ��ʱ����ɣ�ȫ������㿪ʼ���У�
			if (HP_This_Run_Time_Cnt>=HP_Setup_Total_Time)
			{
	//printf("\r\n ʱ������ʱ�����:%d",(int)(HP_This_Run_Time_Cnt));
		  		HP_This_Run_Time_Cnt=0;
				HP_Preview_Status=FALSE;
				DLP.PSta=Pause;
				//**************************
			}	
			//---------------------
		}
		//------------------------------------
	}
	
	void HP_Preview_ExecutionTime_OneGroup(int group)//Mask2017_08_31A1
	{
		int tmp;
		int zone_index;
		int work_status;
		//uchar group=1;
		int block;
		int Refresh_dmx512=0;

		int  dy;
		int  TimeDiff;
		int split_step;
		int front_step;
		int front_block;
		int ch;
		int group_lamps;//��Ƹ���

		//for(tmp=0;tmp<LAMPGROUP_MAX;tmp++)
		//�ֶ���̵�ǰ��ִ����ʱֵ
	    zone_index=group-1;
	    
	    block=1;//HP_This_BlockNo_Tabl[zone_index];//30�� ��ǰ���е��ĸ�ʱ��Ƭ���±꡷
		//*******************************
		//��Ϊ���ʱ��ȡС������
		TimeDiff=HP_Setup_SplitIntervalTime_Tabl[zone_index];
		if ( (HP_Cfg_Tabl[zone_index].s.split!=SPLIT_NULL)&&(TimeDiff!=0) )//��Ϊ��ִ���
		{
			//���С������ MODI2016_11_09A5
		    TimeDiff=HP_This_SplitIntervalTime_Tabl[zone_index];

//printf("\r\n С��TimeDiffʱ���=:%d",(int)(TimeDiff));//MODI2016_11_08A4
//printf("\r\n С��SplitStepNOʱ���=:%d",(int)(HP_This_SplitStepNO_Tabl[zone_index]));//MODI2016_11_08A4	

			if (TimeDiff>0) TimeDiff--;
			HP_This_SplitIntervalTime_Tabl[zone_index]=TimeDiff;
			if (TimeDiff==0)//��ֵ���ʱ��ʱ����ʼ����1С����
			{
				Refresh_dmx512=1;
//printf("\r\n С��ִ��ʱ�䵽=:%d",(int)(1));
				//-----------------------------------
				HP_This_SplitIntervalTime_Tabl[zone_index]=HP_Setup_SplitIntervalTime_Tabl[zone_index];
				split_step=HP_This_SplitStepNO_Tabl[zone_index];

				split_step++;

//printf("\r\n ���Ե�split setup interval=:%d",(int)(HP_Setup_SplitIntervalTime_Tabl[zone_index]));//MODI2016_11_08A4
//printf("\r\n ���Ե�split cuu step=:%d",(int)(split_step));//MODI2016_11_08A4
//printf("\r\n ���Ե�split total=:%d",(int)(HP_Setup_SplitStepTotal_Tabl[zone_index]));//MODI2016_11_08A4

				if (split_step>HP_Setup_SplitStepTotal_Tabl[zone_index])
				{
			   		split_step=1;	
				}
				HP_This_SplitStepNO_Tabl[zone_index]=split_step;

//printf("\r\n ���Ե�split little step=:%d",(int)(split_step));//MODI2016_11_08A4

				//ִ�в�ֲ�����
				front_block=PreviewBlock;//HP_This_BlockNo_Tabl[zone_index];
				front_step=HP_This_StepNo_Tabl[zone_index]; //��ǰ���в����� 
				Execution_Split_LittleStep_To_Buf(group,front_block,front_step,HP_playbuf);
				//-----------------------------------
			}
		}

		//*********************************
		//MARK2017_03_01
		//����֣�ʱ�����0�����
		if(HP_Split_TimeDiff_Flag==1)
		if((HP_Cfg_Tabl[zone_index].s.split==SPLIT_NULL)&&(TimeDiff!=0))//�������ʱ�Ϊ0
		{
			//TimeDiff=HP_Convert_TimeDiff(&HP_Cfg_Tabl[zone_index].buffer[2]);
			TimeDiff=HP_This_SplitIntervalTime_Tabl[zone_index];

			if (TimeDiff>0) TimeDiff--;
//printf("\r\n ����֣�ʱ�����0TimeDiff=:%d",(int)(TimeDiff));		
			HP_This_SplitIntervalTime_Tabl[zone_index]=TimeDiff;
			
			if(TimeDiff==0)
			{
//printf("\r\n ����֣�ʱ�����0=:%d",(int)(1));
				Refresh_dmx512=1;

				HP_This_SplitIntervalTime_Tabl[zone_index]=HP_Setup_SplitIntervalTime_Tabl[zone_index];

				group_lamps=Get_Group_Lamps(group);	//ȡ����Ƹ���
//printf("\r\n ��Ƹ���group_lamps=:%d",(int)(group_lamps));			
				split_step=HP_This_SplitStepNO_Tabl[zone_index];
//printf("\r\n split_step=:%d",(int)(split_step));
				split_step++;			

				if (split_step>group_lamps)
				{
			   		split_step=1;	
				}
				HP_This_SplitStepNO_Tabl[zone_index]=split_step;
//printf("\r\n split_step=:%d",(int)(split_step));	
				//ִ�в�ֲ�����
				front_block=HP_This_BlockNo_Tabl[zone_index];
				front_step=HP_This_StepNo_Tabl[zone_index]; //��ǰ���в����� 
				Execution_Split_TimeDiff_To_Buf(group,front_block,front_step,HP_playbuf);
			}		 
		}
		//*********************************
		//*******************************			               
		//��鲽ʱ�䣺��������ȡһ��
		dy=HP_This_ExecTime_Tabl[zone_index];
		if (dy>0) dy--; 
//printf("\r\n ����������ȡһ��dy=:%d",(int)(dy));
		HP_This_ExecTime_Tabl[zone_index]=dy;

		if (dy==0)//ִ��ʱ�䵽ʱ
		{
			HP_Split_TimeDiff_Flag=1;//MARK2017_03_01

//MODI2017_03_14A1
//printf("\r\n HP��ִ��ʱ�䵽=:%d",(int)(1));

//printf("\r\n dddddd---HPִ��ʱ�䵽ʱ:%d",(int)(1));//MODI2016_12_20A3
//printf("\r\n dddddd---HP���Ե�group=:%d",(int)(group));//MODI2016_12_20A3
//printf("\r\n dddddd---HP���Ե�block=:%d",(int)(block));//MODI2016_12_20A3

			Refresh_dmx512=1;
			//--------------------------------------
			//�������ǰ�����������ݣ��������ݣ����֣�ʱ�����ϣ�
			//ִ��ʱ�䵽ʱ�����У�
			HP_Execute_This_Step_To_Buf(group,HP_playbuf);
			//--------------------------------------
		}
		
		if (Refresh_dmx512==1)
		{
			Refresh_dmx512=0;
			HP_NewData_Reflash=TRUE;   //�ֶ����ˢ�� Ϊ1Ҫˢ�� 0���ã�
		}
	}*/
}
