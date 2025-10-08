# templates/_helpers.tpl
{{/* Generate a name using the release name */}}
{{- define "trustees-service.fullname" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{- define "trustees-service.name" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

