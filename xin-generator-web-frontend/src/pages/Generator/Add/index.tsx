import FileUploader from '@/components/FileUploader';
import PictureUploader from '@/components/PictureUploader';
import { COS_HOST } from '@/constants';
import { addGeneratorUsingPost, editGeneratorUsingPost, getGeneratorVoByIdUsingGet, updateGeneratorUsingPost } from '@/services/backend/generatorController';
import type { ProFormInstance } from '@ant-design/pro-components';
import {
  ProCard,
  ProFormItem,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  StepsForm,
} from '@ant-design/pro-components';
import { history, useSearchParams } from '@umijs/max';
import { message, UploadFile } from 'antd';
import { values } from 'lodash';
import { useEffect, useRef, useState } from 'react';
import ModelConfig from '../Detail/components/ModelConfig';
import ModelConfigForm from './components/ModelConfigForm';


/**
 * 创建生成器页面
 * @returns 页面
 */
const GeneratorAddPage: React.FC = () => {
  //获取路径上的id
  const [searchParams] = useSearchParams();
  const id:number = Number(searchParams.get("id"));
  const [oldData, setOldData] = useState<API.GeneratorEditRequest>();
  
  const loadData = async () => {
    // console.log("调用了")
    //若是id不存在，直接返回
    if (!id) {
      return;
    }
    //若是id存在，调用后端接口进行数据查询
    try{
      const res = await getGeneratorVoByIdUsingGet({id})
      //处理文件（此处获取到的文件为文件路径，需要重新封装成前端展示的UploadFile）
      if (res.data) {
        const {distPath} = res.data;
        if (distPath) {
          //@ts-ignore
          res.data.distPath = [{
            uid: id,
            name: '文件'+id,
            status: 'done',
            url: COS_HOST + distPath,
            response: distPath,
        }as UploadFile];
        }
      }
      setOldData(res.data)
      //set好像有延迟
      // formRef.current?.setFieldsValue(res.data)
   
    } catch (error: any) {
      message.error("修改失败，"+ error.message)
    }
  };

  useEffect(() => {
    if (!id){
      return;
    }
    loadData();
  }, [id])


  const formRef = useRef<ProFormInstance>();
  const doAdd = async (values: API.GeneratorAddRequest) => {
    //调用后端接口
    try {
      const res = await addGeneratorUsingPost(values)
      if (res.data) {
          message.success("创建成功")
          history.push(`/generator/detail/${res.data}`)
      } 
  } catch (error: any) {
      message.error("创建失败" + error.message)
  }
  }
  const doUpdate = async (values: API.GeneratorAddRequest) => {
    //调用后端接口
    try {
      const res = await editGeneratorUsingPost(values)
      if (res.data) {
          message.success("更新成功")
          history.push(`/generator/detail/${id}`)
      } 
  } catch (error: any) {
      message.error("更新失败" + error.message)
  }
  }

  //提交
  const doSubmit = async (values: API.GeneratorAddRequest) => {
    //数据转换
    if (!values.fileConfig) {
        values.fileConfig = {};
    }
    if (!values.modelConfig) {
        values.modelConfig = {};
    }
    //文件列表转化成url
    if (values.distPath && values.distPath.length > 0) {
        //@ts-ignore
        values.distPath = values.distPath[0].response;

    }
    //调用后端接口
    if (id) {
      await doUpdate({
        id,
        ...values
      })
    }else{
      await doAdd({
        ...values
      })
    }

  }
  return (
    <ProCard>
      {(!id || oldData) && (
      <StepsForm<API.GeneratorAddRequest>
        formRef={formRef}
        formProps={{initialValues: oldData}}
        onFinish={doSubmit}
      >
        <StepsForm.StepForm<{
          name: string;
        }>
          name="base"
          title="基本信息"
          stepProps={{
            description: '这里填入的都是基本信息',
          }}
          onFinish={async () => {
            // console.log(formRef.current?.getFieldsValue());
            return true;
          }}
        >
            <ProFormText name='name' label='名称' placeholder='请输入名称'></ProFormText>
            <ProFormTextArea name='description' label='描述' placeholder='请输入描述'></ProFormTextArea>
            <ProFormText name='basePackage' label='基础包' placeholder='请输入基础包'></ProFormText>
            <ProFormText name='version' label='版本' placeholder='请输入版本'></ProFormText>
            <ProFormText name='author' label='作者' placeholder='请输入作者'></ProFormText>
            <ProFormSelect name='tags' mode='tags' label='标签' placeholder='请输入标签列表'></ProFormSelect>
            <ProFormItem label='图片' name='picture'>
                <PictureUploader biz='generator_picture'/>
            </ProFormItem>
        </StepsForm.StepForm>
        <StepsForm.StepForm
          name="fileConfig"
          title="文件配置"
        >
        {/* todo 待补充 */}
        </StepsForm.StepForm>

        <StepsForm.StepForm
          name="modelConfig"
          title="模型配置"
          onFinish={async (values) => {
            // console.log(values);
            return true;
          }}  
        >
        {/* todo 待补充 */}
        <ModelConfigForm formRef={formRef} oldData={oldData}/>
        </StepsForm.StepForm>

        <StepsForm.StepForm
          name="dist"
          title="生成器文件"
        >
            <ProFormItem label='产物包' name='distPath'>
                <FileUploader biz='generator_dist' description='请上传生成器文件压缩包！'></FileUploader>
            </ProFormItem>
        </StepsForm.StepForm>
      </StepsForm>
      )} 
    </ProCard>
  );
};
export default GeneratorAddPage;