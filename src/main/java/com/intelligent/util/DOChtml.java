//package com.intelligent.util;
//
//public class DOChtml {
//    /**
//     * DOC文档解析
//     *
//     * @param inputStream
//     *            输入流
//     * @throws Exception
//     *             异常
//     */
//    private AnalysisDTO docToHtml(InputStream inputStream) throws Exception {
//
//        BufferedReader bf = null;
//
//        List<AnalysisPicMsgDTO> ossList = Collections.synchronizedList(new ArrayList<>());
//
//        try {
//
//            HWPFDocument wordDocument = new HWPFDocument(inputStream);
//
//            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
//
//            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(document);
//
//            /**
//             * 1.保存图片，并返回图片的相对路径（为异步）
//             *
//             * 2.content 二进制的图片文件
//             *
//             * 3.pictureType图片类型（后缀）
//             *
//             * 4.name 文件名称
//             */
//            wordToHtmlConverter.setPicturesManager((content, pictureType, name, width, height) -> {
//
//                String attachmentUrl = "";
//                // oss获取成功
//                if (ret.isSuccess()) {
//                    attachmentUrl = (String) ret.getResult().getData();
//                }
//                // 返回上传后的阿里云oss路径，并替换到文档处
//                return attachmentUrl;
//            });
//
//            wordToHtmlConverter.processDocument(wordDocument);
//
//            Document htmlDocument = wordToHtmlConverter.getDocument();
//
//            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//
//            DOMSource domSource = new DOMSource(htmlDocument);
//
//            StreamResult streamResult = new StreamResult(outStream);
//
//            // 获取转化工厂实例
//            TransformerFactory tf = TransformerFactory.newInstance();
//
//            Transformer serializer = tf.newTransformer();
//
//            // 设置格式，编码，缩进信息，并执行转换
//            setLayOutMsg(serializer, domSource, streamResult, "UTF-8");
//
//            outStream.close();
//            String content = new String(outStream.toByteArray(), "UTF-8");
//
//            // 去除样式(去除HTML的style样式，不要要的话，本句可以删除)
//            String strContent = CommonUtil.delDangerHTMLTag(content);
//
//            AnalysisDTO dto = new AnalysisDTO();
//            dto.setHtmlFile(strContent);
//            dto.setOssKeyList(ossList);
//
//            return dto;
//        } catch (FileNotFoundException e) {
//
//            LOG.error(EventLog.cast(LogType.DOCUMENT, "解析【doc】文档时，未找到文档"), e);
//            throw new ServiceException(ErrorCodes.NOT_FIND_DOC);
//        } catch (IOException e) {
//
//            LOG.error(EventLog.cast(LogType.DOCUMENT, "解析【doc】文档为html时，出现IO异常"), e);
//            throw new ServiceException(ErrorCodes.ANALYSIS_DOC_IO_EXCEPTION);
//        } catch (ParserConfigurationException e) {
//
//            LOG.error(EventLog.cast(LogType.DOCUMENT, "解析【doc】文档为html时，解析器配置异常"), e);
//            throw new ServiceException(ErrorCodes.PARSER_CONFIGURATION_EXCEPTION);
//        } catch (TransformerConfigurationException e) {
//
//            LOG.error(EventLog.cast(LogType.DOCUMENT, "解析【doc】文档为html时，变压器配置异常"), e);
//            throw new ServiceException(ErrorCodes.TRANSFORMER_CONFIGURATION_EXCEPTION);
//        } catch (TransformerFactoryConfigurationError e) {
//
//            LOG.error(EventLog.cast(LogType.DOCUMENT, "解析【doc】文档为html时，变压器出厂配置错误"), e);
//            throw new ServiceException(ErrorCodes.PARSER_CONFIGURATION_EXCEPTION);
//        } catch (TransformerException e) {
//
//            LOG.error(EventLog.cast(LogType.DOCUMENT, "解析【doc】文档为html时，变压器异常"), e);
//            throw new ServiceException(ErrorCodes.PARSER_CONFIGURATION_EXCEPTION);
//        } catch (Exception e) {
//
//            LOG.error(EventLog.cast(LogType.DOCUMENT, "解析【doc】文档为html时，出现普通异常"), e);
//            throw new ServiceException(ErrorCodes.ANALYSIS_DOC_EXCEPTION);
//        } finally {
//
//            if (null != bf) {
//                bf.close();
//            }
//            if (null != inputStream) {
//                inputStream.close();
//            }
//        }
//    }
//
//
//
//    /**
//     * 设置格式，编码，缩进等信息
//     */
//    private void setLayOutMsg(Transformer serializer, DOMSource domSource, StreamResult streamResult, String fileCode) throws Exception {
//
//        String defaultCode = serializer.getOutputProperties().getProperty(OutputKeys.ENCODING);
//
//        LOG.info("JAVA默认编码格式为：" + defaultCode);
//
//        // 编码
//        serializer.setOutputProperty(OutputKeys.ENCODING, fileCode);
//
//        // 缩进
//        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
//
//        // 格式
//        serializer.setOutputProperty(OutputKeys.METHOD, "html");
//
//        serializer.transform(domSource, streamResult);
//    }
//
//}
