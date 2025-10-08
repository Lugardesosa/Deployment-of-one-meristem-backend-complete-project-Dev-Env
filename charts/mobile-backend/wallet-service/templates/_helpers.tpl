# templates/_helpers.tpl
{{/* Generate a name using the release name */}}
{{- define "wallet-service.fullname" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{- define "wallet-service.name" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

