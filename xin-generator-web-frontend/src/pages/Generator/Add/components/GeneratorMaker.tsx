import FileUploader from "@/components/FileUploader";
import { makeGeneratorUsingPost } from "@/services/backend/generatorController";
import { ProForm, ProFormInstance, ProFormItem } from "@ant-design/pro-components";
import { Collapse, message } from "antd";
import { saveAs } from "file-saver";
import { useRef } from "react";

interface Props{
    meta: any;
}

export default (props: Props) => {
    const {meta} = props;
    const formRef = useRef<ProFormInstance>();
      //提交
    const doSubmit = async (values: API.GeneratorMakeRequest) => {
        //数据转换
        if (!meta.name) {
            message.error("请填写名称");
            return;
        }
        
        const zipFilePath = values.zipFilePath;
        if (!zipFilePath || zipFilePath.length < 1) {
            message.error("请上传模板文件压缩包")
            return;
        }
        //文件列表转化成url
        //@ts-ignore
        values.zipFilePath = zipFilePath[0].response;
        try{
            //调用接口
            const blob = await makeGeneratorUsingPost(
                {
                    meta,
                    zipFilePath: values.zipFilePath
                },
                {
                    responseType: 'blob'
                }
            );
            saveAs(blob,meta.name + ".zip")
        }catch(error: any){
            message.error("制作失败,"+error.message);
        }
        
    }
    //表单视图
    const formView = (
        <ProForm
         onFinish={doSubmit}
         formRef={formRef} submitter={{
            searchConfig: {
                submitText: "制作",
            },
            resetButtonProps: {
                hidden:true
            }
        }}>
            <ProFormItem label="模板文件" name="zipFilePath">
                <FileUploader biz="generator_make_template" description="请上传压缩包（注意：打包时请不要加上最外层的目录！）"></FileUploader>
            </ProFormItem>
        </ProForm>
    )
    return <Collapse
     style={{marginBottom: 24}}
     items={[{
        key: 'maker',
        label: '生成器制作工具',
        children: formView
    }]}/>
};