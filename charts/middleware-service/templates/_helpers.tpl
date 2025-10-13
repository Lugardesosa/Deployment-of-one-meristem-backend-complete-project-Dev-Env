# templates/_helpers.tpl
{{/* Generate a name using the release name */}}
{{- define "middleware-service.fullname" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{- define "middleware-service.name" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

